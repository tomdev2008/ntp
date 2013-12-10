
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package cn.me.xdf.webservice.client.ad;

import java.util.logging.Logger;

/**
 * This class was generated by Apache CXF 2.4.2
 * 2013-03-14T19:37:39.590+08:00
 * Generated source version: 2.4.2
 * 
 */

@javax.jws.WebService(
                      serviceName = "ADApi",
                      portName = "ADApiSoap12",
                      targetNamespace = "http://tempuri.org/",
                      wsdlLocation = "http://192.168.1.12:866/ADApi.asmx?wsdl",
                      endpointInterface = "ADApiSoap")
                      
public class ADApiSoapImpl1 implements ADApiSoap {

    private static final Logger LOG = Logger.getLogger(ADApiSoapImpl.class.getName());

    /* (non-Javadoc)
     * @see ADApiSoap#updateUserthumbnailphotoV2(java.lang.String  accountID ,)byte[]  thumbnailphoto ,)java.lang.String  systemTag ,)java.lang.String  sign )*
     */
    @Override
	public StateInfo updateUserthumbnailphotoV2(String accountID,byte[] thumbnailphoto,String systemTag,String sign) {
        LOG.info("Executing operation updateUserthumbnailphotoV2");
        System.out.println(accountID);
        System.out.println(thumbnailphoto);
        System.out.println(systemTag);
        System.out.println(sign);
        try {
            StateInfo _return = new StateInfo();
            ExtensionDataObject _returnExtensionData = new ExtensionDataObject();
            _return.setExtensionData(_returnExtensionData);
            _return.setErrMessage("ErrMessage945052537");
            _return.setState(1013558339);
            _return.setIExit(504241598);
            _return.setSExit("SExit1570544723");
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see ADApiSoap#addADAccountForTemp(UserInfo  userInfo )*
     */
    @Override
	public StateInfo addADAccountForTemp(UserInfo userInfo) { 
        LOG.info("Executing operation addADAccountForTemp");
        System.out.println(userInfo);
        try {
            StateInfo _return = new StateInfo();
            ExtensionDataObject _returnExtensionData = new ExtensionDataObject();
            _return.setExtensionData(_returnExtensionData);
            _return.setErrMessage("ErrMessage62876448");
            _return.setState(820202203);
            _return.setIExit(198104210);
            _return.setSExit("SExit1814540582");
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see ADApiSoap#helloWorld(*
     */
    @Override
	public String helloWorld() {
        LOG.info("Executing operation helloWorld");
        try {
            String _return = "_return1388360672";
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see ADApiSoap#getUserThumbnailphoto(UserInfo  userInfo )*
     */
    @Override
	public byte[] getUserThumbnailphoto(UserInfo userInfo) { 
        LOG.info("Executing operation getUserThumbnailphoto");
        System.out.println(userInfo);
        try {
            byte[] _return = new byte[] {};
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see ADApiSoap#updateUserPhone(UserInfo  userInfo )*
     */
    @Override
	public StateInfo updateUserPhone(UserInfo userInfo) { 
        LOG.info("Executing operation updateUserPhone");
        System.out.println(userInfo);
        try {
            StateInfo _return = new StateInfo();
            ExtensionDataObject _returnExtensionData = new ExtensionDataObject();
            _return.setExtensionData(_returnExtensionData);
            _return.setErrMessage("ErrMessage-2038254764");
            _return.setState(358534227);
            _return.setIExit(272585855);
            _return.setSExit("SExit268137165");
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see ADApiSoap#disabledUser(UserInfo  userInfo )*
     */
    @Override
	public StateInfo disabledUser(UserInfo userInfo) { 
        LOG.info("Executing operation disabledUser");
        System.out.println(userInfo);
        try {
            StateInfo _return = new StateInfo();
            ExtensionDataObject _returnExtensionData = new ExtensionDataObject();
            _return.setExtensionData(_returnExtensionData);
            _return.setErrMessage("ErrMessage-532643128");
            _return.setState(-508931108);
            _return.setIExit(1034319267);
            _return.setSExit("SExit-1295902643");
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see ADApiSoap#updateUserthumbnailphoto(UserInfo  userInfo )*
     */
    @Override
	public StateInfo updateUserthumbnailphoto(UserInfo userInfo) { 
        LOG.info("Executing operation updateUserthumbnailphoto");
        System.out.println(userInfo);
        try {
            StateInfo _return = new StateInfo();
            ExtensionDataObject _returnExtensionData = new ExtensionDataObject();
            _return.setExtensionData(_returnExtensionData);
            _return.setErrMessage("ErrMessage320673237");
            _return.setState(-1219297978);
            _return.setIExit(1036043775);
            _return.setSExit("SExit-604503301");
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see ADApiSoap#changePWD(java.lang.String  accountID ,)java.lang.String  oldPWD ,)java.lang.String  newPWD ,)java.lang.String  systemTag ,)java.lang.String  sign )*
     */
    @Override
	public StateInfo changePWD(String accountID,String oldPWD,String newPWD,String systemTag,String sign) {
        LOG.info("Executing operation changePWD");
        System.out.println(accountID);
        System.out.println(oldPWD);
        System.out.println(newPWD);
        System.out.println(systemTag);
        System.out.println(sign);
        try {
            StateInfo _return = new StateInfo();
            ExtensionDataObject _returnExtensionData = new ExtensionDataObject();
            _return.setExtensionData(_returnExtensionData);
            _return.setErrMessage("ErrMessage-1034554445");
            _return.setState(202517865);
            _return.setIExit(254438492);
            _return.setSExit("SExit-1600551731");
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see ADApiSoap#openMail(UserInfo  userInfo )*
     */
    @Override
	public StateInfo openMail(UserInfo userInfo) { 
        LOG.info("Executing operation openMail");
        System.out.println(userInfo);
        try {
            StateInfo _return = new StateInfo();
            ExtensionDataObject _returnExtensionData = new ExtensionDataObject();
            _return.setExtensionData(_returnExtensionData);
            _return.setErrMessage("ErrMessage1159616136");
            _return.setState(-1539924061);
            _return.setIExit(54212433);
            _return.setSExit("SExit-1797482888");
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see ADApiSoap#updateUser(UserInfo  userInfo )*
     */
    @Override
	public StateInfo updateUser(UserInfo userInfo) { 
        LOG.info("Executing operation updateUser");
        System.out.println(userInfo);
        try {
            StateInfo _return = new StateInfo();
            ExtensionDataObject _returnExtensionData = new ExtensionDataObject();
            _return.setExtensionData(_returnExtensionData);
            _return.setErrMessage("ErrMessage-588815110");
            _return.setState(-7347753);
            _return.setIExit(2070283001);
            _return.setSExit("SExit-48308974");
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}
