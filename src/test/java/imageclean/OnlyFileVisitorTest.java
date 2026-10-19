package imageclean;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.Assert.assertEquals;

public class OnlyFileVisitorTest {

    private static class Visitor implements OnlyFileVisitor<Path> {
        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) {
            return FileVisitResult.CONTINUE;
        }
    }

    @Test
    public void preVisitDirectoryContinues() throws IOException {
        assertEquals(FileVisitResult.CONTINUE, new Visitor().preVisitDirectory(Path.of("/tmp"), null));
    }

    @Test
    public void postVisitDirectoryContinues() throws IOException {
        assertEquals(FileVisitResult.CONTINUE, new Visitor().postVisitDirectory(Path.of("/tmp"), null));
    }

    @Test
    public void visitFileFailedRethrows() {
        final var visitor = new Visitor();
        final var failure = new IOException("boom");
        try {
            visitor.visitFileFailed(Path.of("/tmp"), failure);
        } catch (final IOException e) {
            assertEquals("boom", e.getMessage());
            return;
        }
        throw new AssertionError("Expected visitFileFailed to rethrow");
    }

    @Test
    public void visitFileIsAbstract() {
        final var visitor = new Visitor();
        // The visitFile method is the only abstract method left
        assertEquals(FileVisitResult.CONTINUE, visitor.visitFile(Path.of("/tmp"), null));
    }
}