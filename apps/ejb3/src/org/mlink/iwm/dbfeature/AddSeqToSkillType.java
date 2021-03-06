package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Nov 15, 2007
 */
public class AddSeqToSkillType extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddSeqToSkillType.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT SKILL_TYPE_REF_SEQ.NEXTVAL FROM dual");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("CREATE SEQUENCE SKILL_TYPE_REF_SEQ MINVALUE 1 MAXVALUE 9999999999999999999999 " +
            		"INCREMENT BY 1 START WITH 1 NOCYCLE");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
