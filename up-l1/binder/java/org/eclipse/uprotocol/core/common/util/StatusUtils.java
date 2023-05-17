package org.eclipse.uprotocol.core.common.util;

import android.os.RemoteException;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.rpc.Code;
import com.google.rpc.Status;
import org.eclipse.uprotocol.core.common.StatusException;

import java.util.Collection;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@SuppressWarnings({"java:S1200", "java:S6212"})
public final class StatusUtils {
    public static final Status STATUS_OK = buildStatus(Code.OK);

    private StatusUtils() {}

    public static boolean isOk(@Nullable Status status) {
        return status != null && status.getCode() == Code.OK_VALUE;
    }

    public static boolean hasCode(@Nullable Status status, int code) {
        return (status != null) && status.getCode() == code;
    }

    public static boolean hasCode(@Nullable Status status, @NonNull Code code) {
        return (status != null) && status.getCode() == code.getNumber();
    }

    public static @NonNull Code getCode(@Nullable Status status, @NonNull Code defaultCode) {
        final Code code = (status != null) ? Code.forNumber(status.getCode()) : defaultCode;
        return (code != null) ? code : defaultCode;
    }

    public static @NonNull Code getCode(@Nullable Status status) {
        return getCode(status, Code.UNKNOWN);
    }

    public static @NonNull Code toCode(int value) {
        final Code code = Code.forNumber(value);
        return (code != null) ? code : Code.UNKNOWN;
    }

    public static @NonNull Status.Builder newStatusBuilder(@NonNull Code code) {
        return Status.newBuilder().setCode(code.getNumber());
    }

    public static @NonNull Status.Builder newStatusBuilder(@NonNull Code code, @Nullable String message) {
        return newStatusBuilder(code).setMessage(message != null ? message : "");
    }

    public static @NonNull Status buildStatus(@NonNull Code code) {
        return newStatusBuilder(code).build();
    }

    public static @NonNull Status buildStatus(@NonNull Code code, @Nullable String message) {
        return newStatusBuilder(code, message).build();
    }

    public static @NonNull Status throwableToStatus(@NonNull Throwable exception) {
        if (exception instanceof StatusException) {
            return ((StatusException) exception).getStatus();
        } else if (exception instanceof CompletionException || (exception instanceof ExecutionException)) {
            final Throwable cause = exception.getCause();
            if (cause instanceof StatusException) {
                return ((StatusException) cause).getStatus();
            } else if (cause != null) {
                return buildStatus(throwableToCode(cause), cause.getMessage());
            }
        }
        return buildStatus(throwableToCode(exception), exception.getMessage());
    }

    @SuppressWarnings({"java:S1541", "java:S3776"})
    private static @NonNull Code throwableToCode(@NonNull Throwable exception) {
        if (exception instanceof SecurityException) {
            return Code.PERMISSION_DENIED;
        } else if (exception instanceof InvalidProtocolBufferException) {
            return Code.INVALID_ARGUMENT;
        } else if (exception instanceof IllegalArgumentException) {
            return Code.INVALID_ARGUMENT;
        } else if (exception instanceof NullPointerException) {
            return Code.INVALID_ARGUMENT;
        } else if (exception instanceof CancellationException) {
            return Code.CANCELLED;
        } else if (exception instanceof IllegalStateException) {
            return Code.UNAVAILABLE;
        } else if (exception instanceof RemoteException) {
            return Code.UNAVAILABLE;
        } else if (exception instanceof UnsupportedOperationException) {
            return Code.UNIMPLEMENTED;
        } else if (exception instanceof InterruptedException) {
            return Code.CANCELLED;
        } else if (exception instanceof TimeoutException) {
            return Code.DEADLINE_EXCEEDED;
        } else {
            return Code.UNKNOWN;
        }
    }

    public static void checkStatusOk(@NonNull Status status) {
        if (!isOk(status)) {
            throw new StatusException(status);
        }
    }

    public static void checkArgument(boolean expression, @Nullable String errorMessage) {
        if (!expression) {
            throw new StatusException(Code.INVALID_ARGUMENT, errorMessage);
        }
    }

    public static void checkArgument(boolean expression, @NonNull Code errorCode, @Nullable String errorMessage) {
        if (!expression) {
            throw new StatusException(errorCode, errorMessage);
        }
    }

    public static int checkArgumentPositive(int value, @Nullable String errorMessage) {
        if (value <= 0) {
            throw new StatusException(Code.INVALID_ARGUMENT, errorMessage);
        }
        return value;
    }

    public static int checkArgumentPositive(int value, @NonNull Code errorCode, @Nullable String errorMessage) {
        if (value <= 0) {
            throw new StatusException(errorCode, errorMessage);
        }
        return value;
    }

    public static int checkArgumentNonNegative(int value, @Nullable String errorMessage) {
        if (value < 0) {
            throw new StatusException(Code.INVALID_ARGUMENT, errorMessage);
        }
        return value;
    }

    public static int checkArgumentNonNegative(int value, @NonNull Code errorCode, @Nullable String errorMessage) {
        if (value < 0) {
            throw new StatusException(errorCode, errorMessage);
        }
        return value;
    }

    public static @NonNull <T extends CharSequence> T checkStringNotEmpty(T string, @Nullable String errorMessage) {
        if (TextUtils.isEmpty(string)) {
            throw new StatusException(Code.INVALID_ARGUMENT, errorMessage);
        }
        return string;
    }

    public static @NonNull <T extends CharSequence> T checkStringNotEmpty(T string, @NonNull Code errorCode,
            @Nullable String errorMessage) {
        if (TextUtils.isEmpty(string)) {
            throw new StatusException(errorCode, errorMessage);
        }
        return string;
    }

    public static @NonNull <T extends CharSequence> T checkStringEquals(T string1, @NonNull T string2,
            @NonNull Code errorCode, @Nullable String errorMessage) {
        if (!TextUtils.equals(string1, string2)) {
            throw new StatusException(errorCode, errorMessage);
        }
        return string1;
    }

    public static @NonNull <T extends CharSequence> T checkStringEquals(T string1, T string2,
            @Nullable String errorMessage) {
        if (!TextUtils.equals(string1, string2)) {
            throw new StatusException(Code.INVALID_ARGUMENT, errorMessage);
        }
        return string1;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static @NonNull <T> T checkNotNull(@Nullable T reference, @Nullable String errorMessage) {
        if (reference == null) {
            throw new StatusException(Code.INVALID_ARGUMENT, errorMessage);
        }
        return reference;
    }

    public static @NonNull <T> T checkNotNull(@Nullable T reference, @NonNull Code errorCode,
            @Nullable String errorMessage) {
        if (reference == null) {
            throw new StatusException(errorCode, errorMessage);
        }
        return reference;
    }

    public static void checkState(boolean expression, @Nullable String errorMessage) {
        if (!expression) {
            throw new StatusException(Code.FAILED_PRECONDITION, errorMessage);
        }
    }

    public static void checkState(boolean expression, @NonNull Code errorCode, @Nullable String errorMessage) {
        if (!expression) {
            throw new StatusException(errorCode, errorMessage);
        }
    }

    public static @NonNull <C extends Collection<T>, T> T checkCollectionContains(
            @NonNull C values, @NonNull T value, @NonNull String valueName) {
        checkNotNull(values, valueName + " '" + value + "' is not supported");
        checkNotNull(value, valueName + " is null");
        if (!values.contains(value)) {
            throw new StatusException(Code.INVALID_ARGUMENT, valueName + " '" + value + "' is not supported");
        }
        return value;
    }

    public static @NonNull String toShortString(@Nullable Status status) {
        if (status == null) {
            return "";
        }
        return "Status{code=" + Code.forNumber(status.getCode()) +
                (TextUtils.isEmpty(status.getMessage()) ? "}" : ", message='" + status.getMessage() + "'}");
    }
}
