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
package com.palantir.common.pooling;

import com.google.common.base.Function;
import com.google.common.collect.ForwardingObject;
import com.palantir.common.base.FunctionCheckedException;

public abstract class ForwardingPoolingContainer<T> extends ForwardingObject implements PoolingContainer<T> {

    @Override
    protected abstract PoolingContainer<T> delegate();

    @Override
    public <V, K extends Exception> V runWithPooledResource(FunctionCheckedException<T, V, K> f)
            throws K {
        return delegate().runWithPooledResource(f);
    }

    @Override
    public <V> V runWithPooledResource(Function<T, V> f) {
        return delegate().runWithPooledResource(f);
    }

    @Override
    public void shutdownPooling() {
        delegate().shutdownPooling();
    }

}
