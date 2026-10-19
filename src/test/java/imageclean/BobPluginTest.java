package imageclean;

import bobthebuildtool.pojos.buildfile.Project;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BobPluginTest {

    private static Project project(final Path dir) {
        return new Project(dir, dir.resolve("bobfile"));
    }

    private static Object invoke(final String name, final Class<?>[] types, final Object... args) throws Exception {
        final Method m = BobPlugin.class.getDeclaredMethod(name, types);
        m.setAccessible(true);
        try {
            return m.invoke(null, args);
        } catch (final InvocationTargetException e) {
            final var cause = e.getCause();
            if (cause instanceof RuntimeException) throw (RuntimeException) cause;
            if (cause instanceof Error) throw (Error) cause;
            throw e;
        }
    }

    @Test
    public void toPathsReturnsDefaultWhenNull() throws Exception {
        final var project = project(Path.of("/tmp/project"));
        final var result = (List<Path>) invoke("toPaths", new Class[]{Project.class, List.class}, project, null);
        assertEquals(List.of(project.parentDir.resolve("src").resolve("main").resolve("images")), result);
    }

    @Test
    public void toPathsThrowsWhenEmpty() throws Exception {
        final var project = project(Path.of("/tmp/project"));
        try {
            invoke("toPaths", new Class[]{Project.class, List.class}, project, List.of());
            fail("Expected IllegalArgumentException");
        } catch (final IllegalArgumentException expected) {
            assertTrue(expected.getMessage().contains("No image paths"));
        }
    }

    @Test
    public void toPathsReturnsGivenPaths() throws Exception {
        final var project = project(Path.of("/tmp/project"));
        final var result = (List<Path>) invoke("toPaths", new Class[]{Project.class, List.class}, project, List.of("foo", "bar"));
        assertEquals(List.of(Paths.get("foo"), Paths.get("bar")), result);
    }

    @Test
    public void processImageSkipsNonImageFiles() throws Exception {
        final var temp = Files.createTempDirectory("imageclean-test");
        final var root = temp.resolve("in");
        Files.createDirectories(root);
        final var file = root.resolve("note.txt");
        Files.writeString(file, "not an image");
        final var classes = temp.resolve("classes");

        invoke("processImage", new Class[]{Path.class, Path.class, Path.class, ExifRewriter.class},
            file, classes, root, new ExifRewriter());

        assertFalse(Files.exists(classes.resolve("note.txt")));
    }

    @Test
    public void processImageCreatesParentDirectories() throws Exception {
        final var temp = Files.createTempDirectory("imageclean-test");
        final var root = temp.resolve("in");
        final var sub = root.resolve("sub");
        Files.createDirectories(sub);
        final var file = sub.resolve("image.jpg");
        Files.writeString(file, "not a real jpeg");
        final var classes = temp.resolve("classes");

        try {
            invoke("processImage", new Class[]{Path.class, Path.class, Path.class, ExifRewriter.class},
                file, classes, root, new ExifRewriter());
            fail("Expected failure while stripping EXIF from a fake JPEG");
        } catch (final Exception expected) {
            // ignore
        }

        assertTrue(Files.exists(classes.resolve("sub")));
    }
}