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

import static org.eclipse.uprotocol.cloudevent.serialize.CloudEventSerializers.PROTOBUF;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.google.rpc.Code;
import org.eclipse.uprotocol.core.common.StatusException;

import java.util.Arrays;

import io.cloudevents.CloudEvent;

/**
 * A parcelable class for serialized CloudEvent.
 */
@SuppressWarnings("java:S6212")
public final class CloudEventProto implements Parcelable {
    @VisibleForTesting
    final byte[] mData;

    public static final Creator<CloudEventProto> CREATOR = new Creator<>() {
        public CloudEventProto createFromParcel(Parcel source) {
            final int size = source.readInt();
            final byte[] data = new byte[size];
            source.readByteArray(data);
            return new CloudEventProto(data);
        }

        public CloudEventProto[] newArray(int size) {
            return new CloudEventProto[size];
        }
    };

    private CloudEventProto(@NonNull byte[] data) {
        mData = data;
    }

    @VisibleForTesting
    CloudEventProto(@NonNull CloudEvent event) {
        mData = PROTOBUF.serializer().serialize(event);
    }

    public static CloudEventProto from(@NonNull CloudEvent event) {
        return new CloudEventProto(event);
    }

    @SuppressWarnings("java:S1166")
    public static @Nullable CloudEvent parseOrNull(@Nullable CloudEventProto data) {
        if (data == null) {
            return null;
        }
        try {
            return PROTOBUF.serializer().deserialize(data.mData);
        } catch (Exception e) {
            return null;
        }
    }

    public static @NonNull CloudEvent parseOrThrow(@Nullable CloudEventProto data) {
        if (data == null) {
            throw new StatusException(Code.INTERNAL, "Data is null");
        }
        try {
            return PROTOBUF.serializer().deserialize(data.mData);
        } catch (Exception e) {
            throw new StatusException(Code.INTERNAL, "Failed to deserialize event", e);
        }
    }

    public @NonNull CloudEvent parse() {
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
        if (!(object instanceof CloudEventProto)) {
            return false;
        }
        final CloudEventProto other = (CloudEventProto) object;
        return Arrays.equals(mData, other.mData);
    }
}
