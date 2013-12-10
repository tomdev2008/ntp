
package cn.me.xdf.webservice.client.ad;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;

/**
 * This class was generated by Apache CXF 2.4.2
 * 2013-03-14T19:37:39.550+08:00
 * Generated source version: 2.4.2
 * 
 */
public final class ADApiSoap_ADApiSoap12_Client {

    private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "ADApi");

    private ADApiSoap_ADApiSoap12_Client() {
    }

    public static void main(String args[]) throws Exception {
        URL wsdlURL = ADApi.WSDL_LOCATION;
        if (args.length > 0 && args[0] != null && !"".equals(args[0])) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        ADApi ss = new ADApi(wsdlURL, SERVICE_NAME);
        ADApiSoap port = ss.getADApiSoap12();  
        
        {
        System.out.println("Invoking updateUserthumbnailphotoV2...");
        String _updateUserthumbnailphotoV2_accountID = "_updateUserthumbnailphotoV2_accountID967009565";
        byte[] _updateUserthumbnailphotoV2_thumbnailphoto = new byte[] {};
        String _updateUserthumbnailphotoV2_systemTag = "_updateUserthumbnailphotoV2_systemTag-1936739594";
        String _updateUserthumbnailphotoV2_sign = "_updateUserthumbnailphotoV2_sign1898979056";
        StateInfo _updateUserthumbnailphotoV2__return = port.updateUserthumbnailphotoV2(_updateUserthumbnailphotoV2_accountID, _updateUserthumbnailphotoV2_thumbnailphoto, _updateUserthumbnailphotoV2_systemTag, _updateUserthumbnailphotoV2_sign);
        System.out.println("updateUserthumbnailphotoV2.result=" + _updateUserthumbnailphotoV2__return);


        }
        {
        System.out.println("Invoking addADAccountForTemp...");
        UserInfo _addADAccountForTemp_userInfo = new UserInfo();
        ExtensionDataObject _addADAccountForTemp_userInfoExtensionData = new ExtensionDataObject();
        _addADAccountForTemp_userInfo.setExtensionData(_addADAccountForTemp_userInfoExtensionData);
        _addADAccountForTemp_userInfo.setAccountID("AccountID609489554");
        _addADAccountForTemp_userInfo.setCompany("Company2038962057");
        _addADAccountForTemp_userInfo.setCompanyNumber("CompanyNumber2117174721");
        _addADAccountForTemp_userInfo.setDepartment("Department-1961752609");
        _addADAccountForTemp_userInfo.setDepartmentNumber("DepartmentNumber-1046543378");
        _addADAccountForTemp_userInfo.setEmployeeID("EmployeeID-1990368836");
        _addADAccountForTemp_userInfo.setInBeijing(-1446028308);
        _addADAccountForTemp_userInfo.setLevel(1868502681);
        _addADAccountForTemp_userInfo.setName("Name-1725543700");
        _addADAccountForTemp_userInfo.setPWD("PWD-461380199");
        _addADAccountForTemp_userInfo.setSign("Sign-2041540836");
        _addADAccountForTemp_userInfo.setSystemTag("SystemTag-62780218");
        _addADAccountForTemp_userInfo.setTelphonenumber("Telphonenumber-320232722");
        byte[] _addADAccountForTemp_userInfoThumbnailphoto = new byte[] {};
        _addADAccountForTemp_userInfo.setThumbnailphoto(_addADAccountForTemp_userInfoThumbnailphoto);
        _addADAccountForTemp_userInfo.setTitle("Title1144994284");
        _addADAccountForTemp_userInfo.setTitleNumber("TitleNumber-1598920359");
        _addADAccountForTemp_userInfo.setValidDate(javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-03-14T19:37:39.562+08:00"));
        StateInfo _addADAccountForTemp__return = port.addADAccountForTemp(_addADAccountForTemp_userInfo);
        System.out.println("addADAccountForTemp.result=" + _addADAccountForTemp__return);


        }
        {
        System.out.println("Invoking helloWorld...");
        String _helloWorld__return = port.helloWorld();
        System.out.println("helloWorld.result=" + _helloWorld__return);


        }
        {
        System.out.println("Invoking getUserThumbnailphoto...");
        UserInfo _getUserThumbnailphoto_userInfo = new UserInfo();
        ExtensionDataObject _getUserThumbnailphoto_userInfoExtensionData = new ExtensionDataObject();
        _getUserThumbnailphoto_userInfo.setExtensionData(_getUserThumbnailphoto_userInfoExtensionData);
        _getUserThumbnailphoto_userInfo.setAccountID("AccountID439693780");
        _getUserThumbnailphoto_userInfo.setCompany("Company-679198576");
        _getUserThumbnailphoto_userInfo.setCompanyNumber("CompanyNumber1973053612");
        _getUserThumbnailphoto_userInfo.setDepartment("Department1433075716");
        _getUserThumbnailphoto_userInfo.setDepartmentNumber("DepartmentNumber-322911018");
        _getUserThumbnailphoto_userInfo.setEmployeeID("EmployeeID-574216268");
        _getUserThumbnailphoto_userInfo.setInBeijing(-850424448);
        _getUserThumbnailphoto_userInfo.setLevel(-584431096);
        _getUserThumbnailphoto_userInfo.setName("Name1251223721");
        _getUserThumbnailphoto_userInfo.setPWD("PWD-1740353830");
        _getUserThumbnailphoto_userInfo.setSign("Sign1130911876");
        _getUserThumbnailphoto_userInfo.setSystemTag("SystemTag-908466864");
        _getUserThumbnailphoto_userInfo.setTelphonenumber("Telphonenumber1623404120");
        byte[] _getUserThumbnailphoto_userInfoThumbnailphoto = new byte[] {};
        _getUserThumbnailphoto_userInfo.setThumbnailphoto(_getUserThumbnailphoto_userInfoThumbnailphoto);
        _getUserThumbnailphoto_userInfo.setTitle("Title-1854463052");
        _getUserThumbnailphoto_userInfo.setTitleNumber("TitleNumber29718860");
        _getUserThumbnailphoto_userInfo.setValidDate(javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-03-14T19:37:39.563+08:00"));
        byte[] _getUserThumbnailphoto__return = port.getUserThumbnailphoto(_getUserThumbnailphoto_userInfo);
        System.out.println("getUserThumbnailphoto.result=" + _getUserThumbnailphoto__return);


        }
        {
        System.out.println("Invoking updateUserPhone...");
        UserInfo _updateUserPhone_userInfo = new UserInfo();
        ExtensionDataObject _updateUserPhone_userInfoExtensionData = new ExtensionDataObject();
        _updateUserPhone_userInfo.setExtensionData(_updateUserPhone_userInfoExtensionData);
        _updateUserPhone_userInfo.setAccountID("AccountID433012600");
        _updateUserPhone_userInfo.setCompany("Company-1918688831");
        _updateUserPhone_userInfo.setCompanyNumber("CompanyNumber-295418631");
        _updateUserPhone_userInfo.setDepartment("Department-658033112");
        _updateUserPhone_userInfo.setDepartmentNumber("DepartmentNumber-970254906");
        _updateUserPhone_userInfo.setEmployeeID("EmployeeID1499036792");
        _updateUserPhone_userInfo.setInBeijing(441025913);
        _updateUserPhone_userInfo.setLevel(709997481);
        _updateUserPhone_userInfo.setName("Name295469147");
        _updateUserPhone_userInfo.setPWD("PWD-1494719766");
        _updateUserPhone_userInfo.setSign("Sign1988922481");
        _updateUserPhone_userInfo.setSystemTag("SystemTag-1034964892");
        _updateUserPhone_userInfo.setTelphonenumber("Telphonenumber1030552741");
        byte[] _updateUserPhone_userInfoThumbnailphoto = new byte[] {};
        _updateUserPhone_userInfo.setThumbnailphoto(_updateUserPhone_userInfoThumbnailphoto);
        _updateUserPhone_userInfo.setTitle("Title-693940283");
        _updateUserPhone_userInfo.setTitleNumber("TitleNumber-1869371616");
        _updateUserPhone_userInfo.setValidDate(javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-03-14T19:37:39.565+08:00"));
        StateInfo _updateUserPhone__return = port.updateUserPhone(_updateUserPhone_userInfo);
        System.out.println("updateUserPhone.result=" + _updateUserPhone__return);


        }
        {
        System.out.println("Invoking disabledUser...");
        UserInfo _disabledUser_userInfo = new UserInfo();
        ExtensionDataObject _disabledUser_userInfoExtensionData = new ExtensionDataObject();
        _disabledUser_userInfo.setExtensionData(_disabledUser_userInfoExtensionData);
        _disabledUser_userInfo.setAccountID("AccountID482261237");
        _disabledUser_userInfo.setCompany("Company-1088541929");
        _disabledUser_userInfo.setCompanyNumber("CompanyNumber1287164311");
        _disabledUser_userInfo.setDepartment("Department-60711015");
        _disabledUser_userInfo.setDepartmentNumber("DepartmentNumber1618745414");
        _disabledUser_userInfo.setEmployeeID("EmployeeID-1606863844");
        _disabledUser_userInfo.setInBeijing(975339189);
        _disabledUser_userInfo.setLevel(639085787);
        _disabledUser_userInfo.setName("Name-71694319");
        _disabledUser_userInfo.setPWD("PWD-1753544632");
        _disabledUser_userInfo.setSign("Sign1578992477");
        _disabledUser_userInfo.setSystemTag("SystemTag52285470");
        _disabledUser_userInfo.setTelphonenumber("Telphonenumber930669987");
        byte[] _disabledUser_userInfoThumbnailphoto = new byte[] {};
        _disabledUser_userInfo.setThumbnailphoto(_disabledUser_userInfoThumbnailphoto);
        _disabledUser_userInfo.setTitle("Title1434972112");
        _disabledUser_userInfo.setTitleNumber("TitleNumber-663851489");
        _disabledUser_userInfo.setValidDate(javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-03-14T19:37:39.566+08:00"));
        StateInfo _disabledUser__return = port.disabledUser(_disabledUser_userInfo);
        System.out.println("disabledUser.result=" + _disabledUser__return);


        }
        {
        System.out.println("Invoking updateUserthumbnailphoto...");
        UserInfo _updateUserthumbnailphoto_userInfo = new UserInfo();
        ExtensionDataObject _updateUserthumbnailphoto_userInfoExtensionData = new ExtensionDataObject();
        _updateUserthumbnailphoto_userInfo.setExtensionData(_updateUserthumbnailphoto_userInfoExtensionData);
        _updateUserthumbnailphoto_userInfo.setAccountID("AccountID-421317777");
        _updateUserthumbnailphoto_userInfo.setCompany("Company718827233");
        _updateUserthumbnailphoto_userInfo.setCompanyNumber("CompanyNumber-754705092");
        _updateUserthumbnailphoto_userInfo.setDepartment("Department-745103752");
        _updateUserthumbnailphoto_userInfo.setDepartmentNumber("DepartmentNumber338517374");
        _updateUserthumbnailphoto_userInfo.setEmployeeID("EmployeeID1641106320");
        _updateUserthumbnailphoto_userInfo.setInBeijing(-2120861862);
        _updateUserthumbnailphoto_userInfo.setLevel(1875507647);
        _updateUserthumbnailphoto_userInfo.setName("Name1007046326");
        _updateUserthumbnailphoto_userInfo.setPWD("PWD-47749424");
        _updateUserthumbnailphoto_userInfo.setSign("Sign-1607201163");
        _updateUserthumbnailphoto_userInfo.setSystemTag("SystemTag-1235957180");
        _updateUserthumbnailphoto_userInfo.setTelphonenumber("Telphonenumber-1815684210");
        byte[] _updateUserthumbnailphoto_userInfoThumbnailphoto = new byte[] {};
        _updateUserthumbnailphoto_userInfo.setThumbnailphoto(_updateUserthumbnailphoto_userInfoThumbnailphoto);
        _updateUserthumbnailphoto_userInfo.setTitle("Title2137644265");
        _updateUserthumbnailphoto_userInfo.setTitleNumber("TitleNumber-1670844217");
        _updateUserthumbnailphoto_userInfo.setValidDate(javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-03-14T19:37:39.568+08:00"));
        StateInfo _updateUserthumbnailphoto__return = port.updateUserthumbnailphoto(_updateUserthumbnailphoto_userInfo);
        System.out.println("updateUserthumbnailphoto.result=" + _updateUserthumbnailphoto__return);


        }
        {
        System.out.println("Invoking changePWD...");
        String _changePWD_accountID = "_changePWD_accountID-1487016779";
        String _changePWD_oldPWD = "_changePWD_oldPWD-1245980642";
        String _changePWD_newPWD = "_changePWD_newPWD1558020425";
        String _changePWD_systemTag = "_changePWD_systemTag289952299";
        String _changePWD_sign = "_changePWD_sign2077487043";
        StateInfo _changePWD__return = port.changePWD(_changePWD_accountID, _changePWD_oldPWD, _changePWD_newPWD, _changePWD_systemTag, _changePWD_sign);
        System.out.println("changePWD.result=" + _changePWD__return);


        }
        {
        System.out.println("Invoking openMail...");
        UserInfo _openMail_userInfo = new UserInfo();
        ExtensionDataObject _openMail_userInfoExtensionData = new ExtensionDataObject();
        _openMail_userInfo.setExtensionData(_openMail_userInfoExtensionData);
        _openMail_userInfo.setAccountID("AccountID549054709");
        _openMail_userInfo.setCompany("Company1987789005");
        _openMail_userInfo.setCompanyNumber("CompanyNumber1085717270");
        _openMail_userInfo.setDepartment("Department-1323693562");
        _openMail_userInfo.setDepartmentNumber("DepartmentNumber-69399255");
        _openMail_userInfo.setEmployeeID("EmployeeID721151039");
        _openMail_userInfo.setInBeijing(-1344392504);
        _openMail_userInfo.setLevel(1039403550);
        _openMail_userInfo.setName("Name837694894");
        _openMail_userInfo.setPWD("PWD-1602291937");
        _openMail_userInfo.setSign("Sign725784765");
        _openMail_userInfo.setSystemTag("SystemTag-1501649638");
        _openMail_userInfo.setTelphonenumber("Telphonenumber79330795");
        byte[] _openMail_userInfoThumbnailphoto = new byte[] {};
        _openMail_userInfo.setThumbnailphoto(_openMail_userInfoThumbnailphoto);
        _openMail_userInfo.setTitle("Title1576195750");
        _openMail_userInfo.setTitleNumber("TitleNumber-448945814");
        _openMail_userInfo.setValidDate(javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-03-14T19:37:39.571+08:00"));
        StateInfo _openMail__return = port.openMail(_openMail_userInfo);
        System.out.println("openMail.result=" + _openMail__return);


        }
        {
        System.out.println("Invoking updateUser...");
        UserInfo _updateUser_userInfo = new UserInfo();
        ExtensionDataObject _updateUser_userInfoExtensionData = new ExtensionDataObject();
        _updateUser_userInfo.setExtensionData(_updateUser_userInfoExtensionData);
        _updateUser_userInfo.setAccountID("AccountID1777583900");
        _updateUser_userInfo.setCompany("Company-1186145910");
        _updateUser_userInfo.setCompanyNumber("CompanyNumber-1992282873");
        _updateUser_userInfo.setDepartment("Department-1712231899");
        _updateUser_userInfo.setDepartmentNumber("DepartmentNumber1053870652");
        _updateUser_userInfo.setEmployeeID("EmployeeID-608941169");
        _updateUser_userInfo.setInBeijing(-157590281);
        _updateUser_userInfo.setLevel(-784607132);
        _updateUser_userInfo.setName("Name-1427195988");
        _updateUser_userInfo.setPWD("PWD286128812");
        _updateUser_userInfo.setSign("Sign981639535");
        _updateUser_userInfo.setSystemTag("SystemTag421804073");
        _updateUser_userInfo.setTelphonenumber("Telphonenumber192608029");
        byte[] _updateUser_userInfoThumbnailphoto = new byte[] {};
        _updateUser_userInfo.setThumbnailphoto(_updateUser_userInfoThumbnailphoto);
        _updateUser_userInfo.setTitle("Title1378596562");
        _updateUser_userInfo.setTitleNumber("TitleNumber-1156061776");
        _updateUser_userInfo.setValidDate(javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-03-14T19:37:39.573+08:00"));
        StateInfo _updateUser__return = port.updateUser(_updateUser_userInfo);
        System.out.println("updateUser.result=" + _updateUser__return);


        }

        System.exit(0);
    }

}
