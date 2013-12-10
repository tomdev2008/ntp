
package cn.me.xdf.webservice.client.ad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UpdateUserResult" type="{http://tempuri.org/}StateInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "updateUserResult"
})
@XmlRootElement(name = "UpdateUserResponse")
public class UpdateUserResponse {

    @XmlElement(name = "UpdateUserResult")
    protected StateInfo updateUserResult;

    /**
     * Gets the value of the updateUserResult property.
     * 
     * @return
     *     possible object is
     *     {@link StateInfo }
     *     
     */
    public StateInfo getUpdateUserResult() {
        return updateUserResult;
    }

    /**
     * Sets the value of the updateUserResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link StateInfo }
     *     
     */
    public void setUpdateUserResult(StateInfo value) {
        this.updateUserResult = value;
    }

}
