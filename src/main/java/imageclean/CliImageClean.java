package imageclean;

import jcli.annotations.CliCommand;
import jcli.annotations.CliOption;
import jcli.annotations.CliPositional;

import java.util.List;

/**
 * The {@code CliImageClean} class.
 */
@CliCommand(name = "imageclean")
public final class CliImageClean {

    /**
     * The {@code noRecursion} field.
     */
    @CliOption(longName = "no-recursion", description = "Disables recursive parsing")
    public boolean noRecursion;

    /**
     * The {@code paths} field.
     */
    @CliPositional
    public List<String> paths;


    /**
     * Creates a new instance.
     */
    public CliImageClean() {
    }
}
