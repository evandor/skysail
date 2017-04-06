package io.skysail.domain.html;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotate entities' fields or methods with this annotation in order to let
 * skysail render it in its generic forms (and to define other things like
 * encryption and HtmlPolicies).
 *
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Field {

    /**
     * selection provider.
     *
     */
    Class<? extends SelectionProvider> selectionProvider() default IgnoreSelectionProvider.class;


    /**
     * http://www.w3.org/TR/REC-html40/interact/forms.html#h-17.4
     */
    InputType inputType() default InputType.TEXT;

    /**
     * Define the poliy for HTML strings.
     *
     */
    HtmlPolicy htmlPolicy() default HtmlPolicy.NO_HTML;

    /**
     * encrypt the field using the passphrase provided by a parameter with this
     * name.
     *
     */
    String encryptWith() default "";

    /**
     * e.g. {"click: 'doThis()'", "focus: 'doThat'"}
     */
    String[] onEvent() default "";

    String[] fieldAttributes() default {};

    String description() default "";

    String cssStyle() default "";

    String cssClass() default "";

}
