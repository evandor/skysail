package io.skysail.server.ext.asciidoctor.test;

import static org.asciidoctor.Asciidoctor.Factory.create;

import java.util.Arrays;
import java.util.HashMap;

import org.asciidoctor.Asciidoctor;
import org.jruby.RubyInstanceConfig;
import org.jruby.javasupport.JavaEmbedUtils;
import org.junit.Test;

public class AsciiDocApplicationTest {

    @Test
    public void testName() {
        RubyInstanceConfig config = new RubyInstanceConfig();
        config.setLoader(this.getClass().getClassLoader());

        JavaEmbedUtils.initialize(Arrays.asList("META-INF/jruby.home/lib/ruby/2.0", "gems/asciidoctor-1.5.4/lib"), config);

        Asciidoctor asciidoctor = create(this.getClass().getClassLoader());
        String html = asciidoctor.convert(
                "Writing AsciiDoc is _easy_!",
                new HashMap<String, Object>());
        System.out.println(html);
    }
}
