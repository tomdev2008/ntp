
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
 * 2013-03-14T19:37:39.480+08:00
 * Generated source version: 2.4.2
 * 
 */
public final class ADApiSoap_ADApiSoap_Client {

    private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "ADApi");

    private ADApiSoap_ADApiSoap_Client() {
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
        ADApiSoap port = ss.getADApiSoap();  
        
        {
        System.out.println("Invoking updateUserthumbnailphotoV2...");
        String _updateUserthumbnailphotoV2_accountID = "_updateUserthumbnailphotoV2_accountID-572294130";
        byte[] _updateUserthumbnailphotoV2_thumbnailphoto = new byte[] {};
        String _updateUserthumbnailphotoV2_systemTag = "_updateUserthumbnailphotoV2_systemTag-1555091689";
        String _updateUserthumbnailphotoV2_sign = "_updateUserthumbnailphotoV2_sign-1543813989";
        StateInfo _updateUserthumbnailphotoV2__return = port.updateUserthumbnailphotoV2(_updateUserthumbnailphotoV2_accountID, _updateUserthumbnailphotoV2_thumbnailphoto, _updateUserthumbnailphotoV2_systemTag, _updateUserthumbnailphotoV2_sign);
        System.out.println("updateUserthumbnailphotoV2.result=" + _updateUserthumbnailphotoV2__return);


        }
        {
        System.out.println("Invoking addADAccountForTemp...");
        UserInfo _addADAccountForTemp_userInfo = new UserInfo();
        ExtensionDataObject _addADAccountForTemp_userInfoExtensionData = new ExtensionDataObject();
        _addADAccountForTemp_userInfo.setExtensionData(_addADAccountForTemp_userInfoExtensionData);
        _addADAccountForTemp_userInfo.setAccountID("AccountID-800539064");
        _addADAccountForTemp_userInfo.setCompany("Company-388181118");
        _addADAccountForTemp_userInfo.setCompanyNumber("CompanyNumber-1368367653");
        _addADAccountForTemp_userInfo.setDepartment("Department-1166456990");
        _addADAccountForTemp_userInfo.setDepartmentNumber("DepartmentNumber-1442874059");
        _addADAccountForTemp_userInfo.setEmployeeID("EmployeeID-576575359");
        _addADAccountForTemp_userInfo.setInBeijing(1427026879);
        _addADAccountForTemp_userInfo.setLevel(-777767832);
        _addADAccountForTemp_userInfo.setName("Name-190341717");
        _addADAccountForTemp_userInfo.setPWD("PWD-1308280668");
        _addADAccountForTemp_userInfo.setSign("Sign924071446");
        _addADAccountForTemp_userInfo.setSystemTag("SystemTag-1575337362");
        _addADAccountForTemp_userInfo.setTelphonenumber("Telphonenumber-794028884");
        byte[] _addADAccountForTemp_userInfoThumbnailphoto = new byte[] {};
        _addADAccountForTemp_userInfo.setThumbnailphoto(_addADAccountForTemp_userInfoThumbnailphoto);
        _addADAccountForTemp_userInfo.setTitle("Title260674101");
        _addADAccountForTemp_userInfo.setTitleNumber("TitleNumber-1853422291");
        _addADAccountForTemp_userInfo.setValidDate(javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-03-14T19:37:39.537+08:00"));
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
        _getUserThumbnailphoto_userInfo.setAccountID("AccountID1845964138");
        _getUserThumbnailphoto_userInfo.setCompany("Company-1843095154");
        _getUserThumbnailphoto_userInfo.setCompanyNumber("CompanyNumber-1909823998");
        _getUserThumbnailphoto_userInfo.setDepartment("Department1186474891");
        _getUserThumbnailphoto_userInfo.setDepartmentNumber("DepartmentNumber-1778807173");
        _getUserThumbnailphoto_userInfo.setEmployeeID("EmployeeID2040615134");
        _getUserThumbnailphoto_userInfo.setInBeijing(-1597300109);
        _getUserThumbnailphoto_userInfo.setLevel(66690426);
        _getUserThumbnailphoto_userInfo.setName("Name815873814");
        _getUserThumbnailphoto_userInfo.setPWD("PWD-380911231");
        _getUserThumbnailphoto_userInfo.setSign("Sign95300575");
        _getUserThumbnailphoto_userInfo.setSystemTag("SystemTag-1825166138");
        _getUserThumbnailphoto_userInfo.setTelphonenumber("Telphonenumber-30131323");
        byte[] _getUserThumbnailphoto_userInfoThumbnailphoto = new byte[] {};
        _getUserThumbnailphoto_userInfo.setThumbnailphoto(_getUserThumbnailphoto_userInfoThumbnailphoto);
        _getUserThumbnailphoto_userInfo.setTitle("Title88045596");
        _getUserThumbnailphoto_userInfo.setTitleNumber("TitleNumber120473114");
        _getUserThumbnailphoto_userInfo.setValidDate(javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-03-14T19:37:39.540+08:00"));
        byte[] _getUserThumbnailphoto__return = port.getUserThumbnailphoto(_getUserThumbnailphoto_userInfo);
        System.out.println("getUserThumbnailphoto.result=" + _getUserThumbnailphoto__return);


        }
        {
        System.out.println("Invoking updateUserPhone...");
        UserInfo _updateUserPhone_userInfo = new UserInfo();
        ExtensionDataObject _updateUserPhone_userInfoExtensionData = new ExtensionDataObject();
        _updateUserPhone_userInfo.setExtensionData(_updateUserPhone_userInfoExtensionData);
        _updateUserPhone_userInfo.setAccountID("AccountID-1423018812");
        _updateUserPhone_userInfo.setCompany("Company-635783022");
        _updateUserPhone_userInfo.setCompanyNumber("CompanyNumber-817955659");
        _updateUserPhone_userInfo.setDepartment("Department-2018737326");
        _updateUserPhone_userInfo.setDepartmentNumber("DepartmentNumber-875953515");
        _updateUserPhone_userInfo.setEmployeeID("EmployeeID-1973788545");
        _updateUserPhone_userInfo.setInBeijing(-1727005030);
        _updateUserPhone_userInfo.setLevel(-1334981605);
        _updateUserPhone_userInfo.setName("Name-2061746772");
        _updateUserPhone_userInfo.setPWD("PWD927198008");
        _updateUserPhone_userInfo.setSign("Sign1477696129");
        _updateUserPhone_userInfo.setSystemTag("SystemTag1933595847");
        _updateUserPhone_userInfo.setTelphonenumber("Telphonenumber1404097676");
        byte[] _updateUserPhone_userInfoThumbnailphoto = new byte[] {};
        _updateUserPhone_userInfo.setThumbnailphoto(_updateUserPhone_userInfoThumbnailphoto);
        _updateUserPhone_userInfo.setTitle("Title509969867");
        _updateUserPhone_userInfo.setTitleNumber("TitleNumber-1772101904");
        _updateUserPhone_userInfo.setValidDate(javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-03-14T19:37:39.541+08:00"));
        StateInfo _updateUserPhone__return = port.updateUserPhone(_updateUserPhone_userInfo);
        System.out.println("updateUserPhone.result=" + _updateUserPhone__return);


        }
        {
        System.out.println("Invoking disabledUser...");
        UserInfo _disabledUser_userInfo = new UserInfo();
        ExtensionDataObject _disabledUser_userInfoExtensionData = new ExtensionDataObject();
        _disabledUser_userInfo.setExtensionData(_disabledUser_userInfoExtensionData);
        _disabledUser_userInfo.setAccountID("AccountID-1015901189");
        _disabledUser_userInfo.setCompany("Company1806414294");
        _disabledUser_userInfo.setCompanyNumber("CompanyNumber485997847");
        _disabledUser_userInfo.setDepartment("Department-1276448034");
        _disabledUser_userInfo.setDepartmentNumber("DepartmentNumber824872574");
        _disabledUser_userInfo.setEmployeeID("EmployeeID176471426");
        _disabledUser_userInfo.setInBeijing(1327620035);
        _disabledUser_userInfo.setLevel(194806426);
        _disabledUser_userInfo.setName("Name868584532");
        _disabledUser_userInfo.setPWD("PWD1692921452");
        _disabledUser_userInfo.setSign("Sign1171131250");
        _disabledUser_userInfo.setSystemTag("SystemTag-1503471892");
        _disabledUser_userInfo.setTelphonenumber("Telphonenumber164989707");
        byte[] _disabledUser_userInfoThumbnailphoto = new byte[] {};
        _disabledUser_userInfo.setThumbnailphoto(_disabledUser_userInfoThumbnailphoto);
        _disabledUser_userInfo.setTitle("Title-137990126");
        _disabledUser_userInfo.setTitleNumber("TitleNumber-1147905740");
        _disabledUser_userInfo.setValidDate(javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-03-14T19:37:39.543+08:00"));
        StateInfo _disabledUser__return = port.disabledUser(_disabledUser_userInfo);
        System.out.println("disabledUser.result=" + _disabledUser__return);


        }
        {
        System.out.println("Invoking updateUserthumbnailphoto...");
        UserInfo _updateUserthumbnailphoto_userInfo = new UserInfo();
        ExtensionDataObject _updateUserthumbnailphoto_userInfoExtensionData = new ExtensionDataObject();
        _updateUserthumbnailphoto_userInfo.setExtensionData(_updateUserthumbnailphoto_userInfoExtensionData);
        _updateUserthumbnailphoto_userInfo.setAccountID("AccountID540359289");
        _updateUserthumbnailphoto_userInfo.setCompany("Company1490223444");
        _updateUserthumbnailphoto_userInfo.setCompanyNumber("CompanyNumber-517087076");
        _updateUserthumbnailphoto_userInfo.setDepartment("Department1841814847");
        _updateUserthumbnailphoto_userInfo.setDepartmentNumber("DepartmentNumber-1840737941");
        _updateUserthumbnailphoto_userInfo.setEmployeeID("EmployeeID686737364");
        _updateUserthumbnailphoto_userInfo.setInBeijing(726067995);
        _updateUserthumbnailphoto_userInfo.setLevel(-131633698);
        _updateUserthumbnailphoto_userInfo.setName("Name-251443375");
        _updateUserthumbnailphoto_userInfo.setPWD("PWD333003580");
        _updateUserthumbnailphoto_userInfo.setSign("Sign1647100437");
        _updateUserthumbnailphoto_userInfo.setSystemTag("SystemTag-1554029144");
        _updateUserthumbnailphoto_userInfo.setTelphonenumber("Telphonenumber-1381517501");
        byte[] _updateUserthumbnailphoto_userInfoThumbnailphoto = new byte[] {};
        _updateUserthumbnailphoto_userInfo.setThumbnailphoto(_updateUserthumbnailphoto_userInfoThumbnailphoto);
        _updateUserthumbnailphoto_userInfo.setTitle("Title1829825430");
        _updateUserthumbnailphoto_userInfo.setTitleNumber("TitleNumber-396149183");
        _updateUserthumbnailphoto_userInfo.setValidDate(javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-03-14T19:37:39.545+08:00"));
        StateInfo _updateUserthumbnailphoto__return = port.updateUserthumbnailphoto(_updateUserthumbnailphoto_userInfo);
        System.out.println("updateUserthumbnailphoto.result=" + _updateUserthumbnailphoto__return);


        }
        {
        System.out.println("Invoking changePWD...");
        String _changePWD_accountID = "_changePWD_accountID-811019019";
        String _changePWD_oldPWD = "_changePWD_oldPWD1366979145";
        String _changePWD_newPWD = "_changePWD_newPWD1352428770";
        String _changePWD_systemTag = "_changePWD_systemTag-842422898";
        String _changePWD_sign = "_changePWD_sign-2002397554";
        StateInfo _changePWD__return = port.changePWD(_changePWD_accountID, _changePWD_oldPWD, _changePWD_newPWD, _changePWD_systemTag, _changePWD_sign);
        System.out.println("changePWD.result=" + _changePWD__return);


        }
        {
        System.out.println("Invoking openMail...");
        UserInfo _openMail_userInfo = new UserInfo();
        ExtensionDataObject _openMail_userInfoExtensionData = new ExtensionDataObject();
        _openMail_userInfo.setExtensionData(_openMail_userInfoExtensionData);
        _openMail_userInfo.setAccountID("AccountID-1801713668");
        _openMail_userInfo.setCompany("Company926424289");
        _openMail_userInfo.setCompanyNumber("CompanyNumber1044668010");
        _openMail_userInfo.setDepartment("Department1432484771");
        _openMail_userInfo.setDepartmentNumber("DepartmentNumber1814312302");
        _openMail_userInfo.setEmployeeID("EmployeeID1446638127");
        _openMail_userInfo.setInBeijing(1077416768);
        _openMail_userInfo.setLevel(710476545);
        _openMail_userInfo.setName("Name461807174");
        _openMail_userInfo.setPWD("PWD1794812055");
        _openMail_userInfo.setSign("Sign-2087384511");
        _openMail_userInfo.setSystemTag("SystemTag-1563078781");
        _openMail_userInfo.setTelphonenumber("Telphonenumber1260876847");
        byte[] _openMail_userInfoThumbnailphoto = new byte[] {};
        _openMail_userInfo.setThumbnailphoto(_openMail_userInfoThumbnailphoto);
        _openMail_userInfo.setTitle("Title-290136414");
        _openMail_userInfo.setTitleNumber("TitleNumber-1998998170");
        _openMail_userInfo.setValidDate(javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-03-14T19:37:39.548+08:00"));
        StateInfo _openMail__return = port.openMail(_openMail_userInfo);
        System.out.println("openMail.result=" + _openMail__return);


        }
        {
        System.out.println("Invoking updateUser...");
        UserInfo _updateUser_userInfo = new UserInfo();
        ExtensionDataObject _updateUser_userInfoExtensionData = new ExtensionDataObject();
        _updateUser_userInfo.setExtensionData(_updateUser_userInfoExtensionData);
        _updateUser_userInfo.setAccountID("AccountID639291849");
        _updateUser_userInfo.setCompany("Company-209145741");
        _updateUser_userInfo.setCompanyNumber("CompanyNumber901773425");
        _updateUser_userInfo.setDepartment("Department-933249354");
        _updateUser_userInfo.setDepartmentNumber("DepartmentNumber585783592");
        _updateUser_userInfo.setEmployeeID("EmployeeID-404831262");
        _updateUser_userInfo.setInBeijing(-451552308);
        _updateUser_userInfo.setLevel(-1648945980);
        _updateUser_userInfo.setName("Name338984928");
        _updateUser_userInfo.setPWD("PWD1852170488");
        _updateUser_userInfo.setSign("Sign7330170");
        _updateUser_userInfo.setSystemTag("SystemTag889633659");
        _updateUser_userInfo.setTelphonenumber("Telphonenumber-74275180");
        byte[] _updateUser_userInfoThumbnailphoto = new byte[] {};
        _updateUser_userInfo.setThumbnailphoto(_updateUser_userInfoThumbnailphoto);
        _updateUser_userInfo.setTitle("Title2126464062");
        _updateUser_userInfo.setTitleNumber("TitleNumber-1153935406");
        _updateUser_userInfo.setValidDate(javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar("2013-03-14T19:37:39.549+08:00"));
        StateInfo _updateUser__return = port.updateUser(_updateUser_userInfo);
        System.out.println("updateUser.result=" + _updateUser__return);


        }

        System.exit(0);
    }

}