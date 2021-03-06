package org.mlink.iwm.dbfeature;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.ObjectDefinition;
import org.mlink.iwm.entity3.TaskDefinition;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.util.DBAccess;

/**
 * Created by IntelliJ IDEA.
 * User: Andrei
 * Date: Mar 20, 2006
 * Time: 3:12:13 PM
 * Checks if database has entries required for processing Defect Reports
 * If not, they (object_def, class, and system_props entry) will be created
 * This needs to be done once per environment.
 */
public class DefectReportSetup extends NewDBFeautureSetup {
    private static final Logger logger = Logger.getLogger(DefectReportSetup.class);

    public boolean isInstalled(){
        boolean isSetup = false;
        String sql = "SELECT 1 FROM SYSTEM_PROPS WHERE PROPERTY='"+ Config.DEFECT_REPORT_OBJECT_DEFINITION_ID + "'";
        try {
            if((DBAccess.execute(sql)).size()>0) isSetup=true;
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // do not even go farther
        }
        return isSetup;
    }

    public void install(){
        if(isInstalled()) return;

        logger.debug("Setting up tables required for External Work Request ");
        long newClassId; String sql;
        try {

            logger.debug("1. find the Area class and use its properties to set up the new class");
            sql = "SELECT OC.ID, OC.CODE, OC.DESCRIPTION,OC.PARENT_ID,OC.ABBR,OC.SCHEMA_ID,OC.DISP_ORD " +
                    "FROM OBJECT_CLASSIFICATION OC,OBJECT_DEF OD,SYSTEM_PROPS SP " +
                    "WHERE OC.ID=OD.CLASS_ID AND OD.ID=SP.VALUE AND SP.PROPERTY='"+Config.AREA_OBJECT_DEFINITION_ID+ "'";
            List result = DBAccess.execute(sql);
            Map areaClass = (Map)result.get(0);

            logger.debug("2. Calculate id for the new class");
            sql = "SELECT MAX(ID) MAX_ID FROM OBJECT_CLASSIFICATION";
            result = DBAccess.execute(sql);
            Map map = (Map)result.get(0);
            newClassId = ((BigDecimal)map.get("MAX_ID")).longValue() + 1;


            logger.debug("3. create new object_classification record ");
            sql = "INSERT INTO OBJECT_CLASSIFICATION " +
                    "( ID,  CODE, DESCRIPTION, PARENT_ID, ABBR, SCHEMA_ID) VALUES (" +
                    newClassId + ",  '" +areaClass.get("CODE") + "','Defect Report',"
                    + areaClass.get("PARENT_ID") + ", 'AreaTarget'," + areaClass.get("SCHEMA_ID") + ")";
            logger.debug(sql);
            DBAccess.executeUpdate(sql);

            logger.debug("4. create new object_def record");
            PolicySF policy = ServiceLocator.getPolicySFLocal();
            ObjectDefinition objDef = new ObjectDefinition();
            objDef.setClassId(newClassId);
            policy.create(objDef);

            logger.debug("5. add " + Config.DEFECT_REPORT_OBJECT_DEFINITION_ID + " to system_props");
            sql="INSERT INTO SYSTEM_PROPS ( PROPERTY,VALUE,DESCRIPTION) VALUES ('"
                    +Config.DEFECT_REPORT_OBJECT_DEFINITION_ID+"',"+objDef.getId()
                    + ",'object definition reserved for defect report requests')";
            logger.debug(sql);
            DBAccess.executeUpdate(sql);

            TaskDefinition vo = new TaskDefinition();
            vo.getObjectDefinition().setId(objDef.getId());
            vo.setTaskDescription("Organizations");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();
            vo.setTaskDescription("People");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();
            vo.setTaskDescription("Skills");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();
            vo.setTaskDescription("Roles");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();
            vo.setTaskDescription("Calendar");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();
            vo.setTaskDescription("WorkSchedule");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();
            vo.setTaskDescription("Locators");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();
            vo.setTaskDescription("Templates");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();
            vo.setTaskDescription("Templates Tasks");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();
            vo.setTaskDescription("Templates Tasks Groups");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();
            vo.setTaskDescription("Objects");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();
            vo.setTaskDescription("Task Sequences");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();

            vo.setTaskDescription("Jobs");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();
            vo.setTaskDescription("Job Schedule");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();
            vo.setTaskDescription("Job Tasks");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();
            vo.setTaskDescription("Job Actions");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();
            vo.setTaskDescription("Job Task Time");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();
            vo.setTaskDescription("Job Projects");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();

            vo.setTaskDescription("Mobile Worker");
            policy.createTaskDefinition(vo); 
            vo = new TaskDefinition();
            vo.setTaskDescription("Maintenance Request");
            policy.createTaskDefinition(vo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

