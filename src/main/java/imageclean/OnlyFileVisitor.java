package imageclean;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * The {@code OnlyFileVisitor} interface.
 *
 * @param <T> the type parameter
 */
public interface OnlyFileVisitor<T> extends FileVisitor<T> {

    /**
     * Returns the result of {@code preVisitDirectory}.
     *
     * @param dir the dir
     * @param attrs the attrs
     * @return the result
     * @throws IOException if the operation fails
     */
    default FileVisitResult preVisitDirectory(T dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    /**
     * Returns the result of {@code visitFileFailed}.
     *
     * @param file the file
     * @param exc the exc
     * @return the result
     * @throws IOException if the operation fails
     */
    default FileVisitResult visitFileFailed(T file, IOException exc) throws IOException {
        throw exc;
    }

    /**
     * Returns the result of {@code postVisitDirectory}.
     *
     * @param dir the dir
     * @param exc the exc
     * @return the result
     * @throws IOException if the operation fails
     */
    default FileVisitResult postVisitDirectory(T dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

}
