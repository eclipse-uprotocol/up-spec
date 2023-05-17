/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.eclipse.uprotocol.core.common;

import static org.eclipse.uprotocol.core.common.util.StatusUtils.buildStatus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.rpc.Code;
import com.google.rpc.Status;

@SuppressWarnings("java:S6212")
public class StatusException extends RuntimeException {
    private final Status mStatus;

    public StatusException(Code code, String message) {
        this(buildStatus(code, message), null);
    }

    public StatusException(Code code, String message, Throwable cause) {
        this(buildStatus(code, message), cause);
    }

    public StatusException(Status status) {
        this(status, null);
    }

    public StatusException(Status status, Throwable cause) {
        super((status != null) ? status.getMessage() : "", cause);
        mStatus = (status != null) ? status : buildStatus(Code.UNKNOWN);
    }

    public @NonNull Status getStatus() {
        return mStatus;
    }

    public @NonNull Code getCode() {
        final Code code = Code.forNumber(mStatus.getCode());
        return (code != null) ? code : Code.UNKNOWN;
    }

    @Override
    public @Nullable String getMessage() {
        return (mStatus != null) ? mStatus.getMessage() : super.getMessage();
    }
}
