/**
 * Copyright 2015 Palantir Technologies
 *
 * Licensed under the BSD-3 License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.palantir.lock;

import java.util.Comparator;

import javax.annotation.Nullable;


/**
 * A lock server token with an expiration date.
 *
 * @author jtamer
 */
public interface ExpiringToken {

    /**
     * Returns the time (in milliseconds since the epoch) since this token was
     * created.
     */
    long getCreationDateMs();

    /**
     * Returns the time, in milliseconds since the epoch, when this token will
     * expire and become invalid.
     */
    long getExpirationDateMs();

    /**
     * Returns the client who holds these locks, or {@code null} if this
     * represents a lock grant.
     */
    @Nullable LockClient getClient();

    /**
     * Returns the amount of time that it takes for these locks to
     * expire.
     */
    TimeDuration getLockTimeout();

    /**
     * Returns the version ID for this token, or {@code null} if no version ID
     * was specified.
     */
    @Nullable Long getVersionId();

    /** A comparator which uses an {@code ExpiringToken}'s expiration date. */
    static final Comparator<ExpiringToken> COMPARATOR = new Comparator<ExpiringToken>() {
        @Override
        public int compare(ExpiringToken o1, ExpiringToken o2) {
            return Long.valueOf(o1.getExpirationDateMs()).compareTo(o2.getExpirationDateMs());
        }
    };
}
