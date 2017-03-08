package io.skysail.api.doc;

import java.lang.reflect.Method;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@Getter
public class ApiMetadata {

    private String summaryForGet;
    private String summaryForPost;
    private String summaryForPut;
    private String summaryForDelete;

    private String descriptionForGet;
    private String descriptionForPost;
    private String descriptionForPut;
    private String descriptionForDelete;

    private String[] tagsForGet;
    private String[] tagsForPost;
    private String[] tagsForPut;
    private String[] tagsForDelete;

    public static class ApiMetadataBuilder {

        public ApiMetadataBuilder summaryForGet(Class<?> cls, String methodName) {
            Method method = getMethod(cls,methodName);
            summaryForGet = getValueOrNullForApiSummary(method);
            return this;
        }

        public ApiMetadataBuilder descriptionForGet(Class<?> cls, String methodName) {
            Method method = getMethod(cls,methodName);
            descriptionForGet = getValueOrNullForApiDescription(method);
            return this;
        }

        public ApiMetadataBuilder tagsForGet(Class<?> cls, String methodName) {
            Method method = getMethod(cls,methodName);
            tagsForGet = getValueOrNullForApiTags(method);
            return this;
        }

        private String getValueOrNullForApiSummary(Method method) {
            if (method != null && method.getDeclaredAnnotation(ApiSummary.class) != null) {
                return method.getDeclaredAnnotation(ApiSummary.class).value();
            }
            return null;
        }

        private String getValueOrNullForApiDescription(Method method) {
            if (method != null && method.getDeclaredAnnotation(ApiDescription.class) != null) {
                return method.getDeclaredAnnotation(ApiDescription.class).value();
            }
            return null;
        }

        private String[] getValueOrNullForApiTags(Method method) {
            if (method != null && method.getDeclaredAnnotation(ApiTags.class) != null) {
                return method.getDeclaredAnnotation(ApiTags.class).value();
            }
            return null;
        }

        private Method getMethod(Class<?> cls,String methodName) {
            try {
                return cls.getDeclaredMethod(methodName);
            } catch (NoSuchMethodException | SecurityException e) {
                log.error(e.getMessage(), e);
            }
            return null;
        }
    }

}
