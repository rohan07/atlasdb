/**
 * // Copyright 2015 Palantir Technologies
 * //
 * // Licensed under the BSD-3 License (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
 * // http://opensource.org/licenses/BSD-3-Clause
 * //
 * // Unless required by applicable law or agreed to in writing, software
 * // distributed under the License is distributed on an "AS IS" BASIS,
 * // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * // See the License for the specific language governing permissions and
 * // limitations under the License.
 */
package com.palantir.example.profile.schema;

import java.io.File;

import com.palantir.atlasdb.schema.AtlasSchema;
import com.palantir.atlasdb.schema.Namespace;
import com.palantir.atlasdb.table.description.IndexDefinition;
import com.palantir.atlasdb.table.description.IndexDefinition.IndexType;
import com.palantir.atlasdb.table.description.Schema;
import com.palantir.atlasdb.table.description.TableDefinition;
import com.palantir.atlasdb.table.description.ValueType;
import com.palantir.example.profile.protos.generated.ProfilePersistence;

public class ProfileSchema implements AtlasSchema {
    public static final AtlasSchema INSTANCE = new ProfileSchema();

    private static final Schema PROFILE_SCHEMA = generateSchema();

    private static Schema generateSchema() {
        Schema schema = new Schema("Profile",
                ProfileSchema.class.getPackage().getName() + ".generated",
                Namespace.EMPTY_NAMESPACE);

        schema.addTableDefinition("user_profile", new TableDefinition() {{
            rowName();
                rowComponent("id", ValueType.FIXED_LONG);
            columns();
                column("metadata", "m", ProfilePersistence.UserProfile.class);
                column("photo_stream_id", "p", ValueType.FIXED_LONG);
        }});

        schema.addIndexDefinition("user_birthdays", new IndexDefinition(IndexType.CELL_REFERENCING) {{
            onTable("user_profile");
            rowName();
                componentFromColumn("birthday", ValueType.VAR_SIGNED_LONG, "metadata", "_value.getBirthEpochDay()");
            dynamicColumns();
                componentFromRow("id", ValueType.FIXED_LONG);
            rangeScanAllowed();
        }});

        schema.addStreamStoreDefinition("user_photos", "user_photos", ValueType.VAR_LONG, 2 * 1024 * 1024);

        return schema;
    }

    public static Schema getSchema() {
        return PROFILE_SCHEMA;
    }

    public static void main(String[]  args) throws Exception {
        PROFILE_SCHEMA.renderTables(new File("src"));
    }

    @Override
    public Schema getLatestSchema() {
        return PROFILE_SCHEMA;
    }

    @Override
    public Namespace getNamespace() {
        return Namespace.EMPTY_NAMESPACE;
    }
}