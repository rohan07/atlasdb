package com.palantir.atlasdb.keyvalue.impl.partition;

import static org.junit.Assert.assertEquals;

import java.util.NavigableMap;

import org.junit.Test;

import com.google.common.primitives.UnsignedBytes;
import com.palantir.atlasdb.keyvalue.impl.InMemoryKeyValueService;
import com.palantir.atlasdb.keyvalue.partition.BasicPartitionMap;
import com.palantir.atlasdb.keyvalue.partition.KeyValueEndpoint;
import com.palantir.atlasdb.keyvalue.partition.PartitionMapService;
import com.palantir.atlasdb.keyvalue.partition.QuorumParameters;
import com.palantir.atlasdb.keyvalue.partition.SimpleKeyValueEndpoint;
import com.palantir.atlasdb.keyvalue.partition.api.PartitionMap;
import com.palantir.atlasdb.keyvalue.partition.util.VersionedObject;

import jersey.repackaged.com.google.common.collect.Maps;

public abstract class AbstractPartitionMapServiceTest {

    protected abstract PartitionMapService getPartitionMapService(VersionedObject<PartitionMap> partitionMap);

    protected static final QuorumParameters QUORUM_PARAMETERS = new QuorumParameters(3, 2, 2);
    protected static final NavigableMap<byte[], KeyValueEndpoint> ring; static {
        ring = Maps.newTreeMap(UnsignedBytes.lexicographicalComparator());
        ring.put(new byte[] {0}, new SimpleKeyValueEndpoint(new InMemoryKeyValueService(false)));
        ring.put(new byte[] {0, 0}, new SimpleKeyValueEndpoint(new InMemoryKeyValueService(false)));
        ring.put(new byte[] {0, 0, 0}, new SimpleKeyValueEndpoint(new InMemoryKeyValueService(false)));
    }
    protected static final PartitionMap samplePartitionMap = BasicPartitionMap.create(QUORUM_PARAMETERS, ring);
    protected static final long initialVersion = 1L;

    @Test
    public void testPms() {
        PartitionMapService pms = getPartitionMapService(VersionedObject.of(samplePartitionMap, initialVersion));
        assertEquals(initialVersion, pms.getVersion());
        assertEquals(samplePartitionMap, pms.get().getObject());

        pms.update(2L, samplePartitionMap);
        assertEquals(2L, pms.getVersion());
    }

}
