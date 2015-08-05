package coolor.fx;

import org.apache.log4j.Logger;

import java.util.ResourceBundle;

/**
 * Created by KAKolesnikov on 2015-08-05.
 */
public abstract class AbstractController {
    protected static final Logger log = Logger.getLogger(AbstractController.class);
    protected ResourceBundle bundle = ResourceBundle.getBundle("application");

    protected Starter starter;

    public Starter getStarter() {
        return starter;
    }

    public void setStarter(Starter starter) {
        this.starter = starter;
    }
}
