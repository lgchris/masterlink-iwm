<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<%-- --%>
<script type="text/javascript" >

    var DataEditPopup = Class.extend( AbstractCrudPopup, {
        initialize: function(popupId, formName, dataTable){
            this.superInit(popupId, formName, dataTable);
        },
        show: function(theId){
            this.theData = new Object();
            if(theId != null && theId != -1){
                $("popupTitle").innerHTML= "Edit Object Data";
                _this = this;
                ObjectsData.getItem(function(response){_this.populate(response);}, theId);
            } else {
                //if no locator id is passed in that means it is an add. make a blank form.
                $("popupTitle").innerHTML= "Add Object Data";
                this.theData.id=-1;
                this.theData.objectId=<%= request.getParameter("id") %>;
                $("isEditInField").checked=true;
                $("isDisplay").checked=true;
                this.theData.custom=1;
                this.populate(this.theData);
            }
            thePopupManager.showPopup('objects-data_edit');
            $('dataLabel').focus();
        },

        populate: function(item){
            //set DWR object
            this.theData = item;
            FormValuesUtil.setFormValues($("DataForm"),this.theData);
            if(this.theData.isEditInField=="1") $("isEditInField").checked=true; else $("isEditInField").checked=false;
            if(this.theData.isDisplay=="1") $("isDisplay").checked=true; else $("isDisplay").checked=false;

            if(this.theData.custom==1){
                $('customize').style.display='none';
                $("custom").checked=true;
                enableEdit(true);
            }else{
                $('customize').style.display='';
                $("custom").checked=false;
                enableEdit(false);
            }
        },


        save: function(){
            if(this.validate()){
                FormValuesUtil.getFormValues($("DataForm"),this.theData);
                var _this = this;
                if($("isDisplay").checked)
                    this.theData.isDisplay="1"; //DWR.getValues for checkboxes returns true/false ignoring actual value. Need to convert
                else
                    this.theData.isDisplay="0";
                if($("isEditInField").checked)
                    this.theData.isEditInField="1"; //DWR.getValues for checkboxes returns true/false ignoring actual value. Need to convert
                else
                    this.theData.isEditInField="0";

                if($("custom").checked)
                    this.theData.custom=1;
                else
                    this.theData.custom=0;

                //ObjectsData.saveItem(function(message){_this.showCallBackMessage(message)}, this.theData);
                this.persist(this.theData);
                return true;
            }
            else {
                return false;
            }
        }
    });


    function enableEdit(value){
        $('dataTypeId').disabled=!value;
        $('dataLabel').disabled=!value;
        $('uomId').disabled=!value;
    }

    function makeCustom(value){
        if(value){
            if(confirm("Changing properties (customization) will detach object data from its template. Changes to the template will not propagate to object data level. Proceed?")){
                enableEdit(true);
                return true;
            }else{
                return false;
            }
        }
    }

callOnLoad(function(){
    theDataEditPopup = new DataEditPopup("objects-data_edit", "DataForm", dataTable);
});
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">

</style>

<!--  -->

<div class="popup" id="objects-data_edit">
    <div class="popupHeader"><h2><span id="popupTitle">Edit Object Data</span></h2></div>

    <div class="popupBody">
        <form id="DataForm" action="" name="DataForm">

            <table>

            <tr>
                <td><label><bean:message key="dataTypeId"/>:</label></td>
                <td>
                    <html:select property="dataTypeId" styleId="dataTypeId" value="">
                        <html:option value="">-- Select Type --</html:option>
                        <html:optionsCollection property="options"  name="DataTypeRef"/>
                    </html:select>
                </td>
            </tr>
            <tr>
                <td><label>Label:</label></td>
                <td><input id="dataLabel" name="dataLabel" type="text"/></td>
            </tr>
            <tr>
                <td><label>Value:</label></td>
                <td><input id="dataValue" name="dataValue" type="text"/></td>
            </tr>
            <tr>
                <td><label><bean:message key="uomId"/>:</label></td>
                <td>
                    <html:select property="uomId" styleId="uomId"  value="">
                        <html:option value="">-- Unit of Measure --</html:option>
                    <html:optionsCollection property="options"  name="UOMRef"/>
                    </html:select>
                </td>
            </tr>
            <tr>
                <td colspan="2" valign="top"><input type="checkbox" id="isDisplay" onclick="$('isEditInField').checked=(this.checked==false?false:$('isEditInField').checked);"><bean:message key="view.in.field"/></td>
            </tr>
            <tr>
                <td colspan="2" valign="top"><input type="checkbox" id="isEditInField" onclick="$('isDisplay').checked=this.checked" ><bean:message key="edit.in.field"/></td>
            </tr>

            <tr id="customize">
                <td colspan="2" valign="top"><input type="checkbox" id="custom" onclick="return makeCustom(this.checked)"><bean:message key="custom"/></td>
            </tr>

        </table>
        <input type="button" class="button" value="Save" onclick="theDataEditPopup.save()"/>
        <input type="button" class="button" value="Cancel" onclick="theDataEditPopup.close();"/>

        </form>
    </div>
</div>


<%/*
Using Validator comes with some rules:
1. As before all validated properties defined in validation.xml
2. You must have a form element in your page  (Validator works with the form)
3. You must have the name atrribute peroperly defined for each validated property (Validator works with names of the form elements)
*/%>
<html:javascript formName="DataForm" dynamicJavascript="true" staticJavascript="false"/>

