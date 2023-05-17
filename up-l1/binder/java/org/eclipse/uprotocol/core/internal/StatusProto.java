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
package org.eclipse.uprotocol.core.internal;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.rpc.Code;
import com.google.rpc.Status;
import org.eclipse.uprotocol.core.common.StatusException;

import java.util.Arrays;

/**
 * A parcelable class for serialized Status.
 */
@SuppressWarnings("java:S6212")
public final class StatusProto implements Parcelable {
    @VisibleForTesting
    final byte[] mData;

    public static final Creator<StatusProto> CREATOR = new Creator<>() {
        public StatusProto createFromParcel(Parcel source) {
            final int size = source.readInt();
            final byte[] data = new byte[size];
            source.readByteArray(data);
            return new StatusProto(data);
        }

        public StatusProto[] newArray(int size) {
            return new StatusProto[size];
        }
    };

    private StatusProto(@NonNull byte[] data) {
        mData = data;
    }

    @VisibleForTesting
    StatusProto(@NonNull Status status) {
        mData = status.toByteArray();
    }

    public static StatusProto from(@NonNull Status status) {
        return new StatusProto(status);
    }

    @SuppressWarnings("java:S1166")
    public static @Nullable Status parseOrNull(@Nullable StatusProto data) {
        if (data == null) {
            return null;
        }
        try {
            return Status.parseFrom(data.mData);
        } catch (InvalidProtocolBufferException e) {
            return null;
        }
    }

    public static @NonNull Status parseOrThrow(@Nullable StatusProto data) {
        if (data == null) {
            throw new StatusException(Code.INTERNAL, "Data is null");
        }
        try {
            return Status.parseFrom(data.mData);
        } catch (InvalidProtocolBufferException e) {
            throw new StatusException(Code.INTERNAL, "Failed to deserialize status", e);
        }
    }

    public @NonNull Status parse() {
        return parseOrThrow(this);
    }

    public @NonNull byte[] getData() {
        return mData;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mData.length);
        dest.writeByteArray(mData);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(mData);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof StatusProto)) {
            return false;
        }
        final StatusProto other = (StatusProto) object;
        return Arrays.equals(mData, other.mData);
    }
}
