 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxsecurity.x509;

import com.mxsecurity.asn1.ASN1AnyDefinedBy;
import com.mxsecurity.asn1.ASN1BitString;
import com.mxsecurity.asn1.ASN1CharString;
import com.mxsecurity.asn1.ASN1OID;
import com.mxsecurity.asn1.ASN1OIDRegistry;
import com.mxsecurity.asn1.ASN1Object;
import com.mxsecurity.asn1.ASN1PrintableString;
import com.mxsecurity.asn1.ASN1Sequence;

/**
 * An attribute OID together with a value whose format is defined by
 * the IOD.
 *
 * <pre>
 *   AttributeTypeAndValue ::= SEQUENCE {
 *     type     AttributeType,
 *     value    AttributeValue }
 *
 *   AttributeType ::= OBJECT IDENTIFIER
 *
 *   AttributeValue ::= ANY DEFINED BY AttributeType
 * </pre>
 */
public final class AttributeTypeAndValue extends ASN1Sequence {

    public ASN1OID          type;
    public ASN1AnyDefinedBy value;

    public AttributeTypeAndValue() {
        type  = new ASN1OID();
        value = new ASN1AnyDefinedBy(type, true);
        addComponent(type);
        addComponent(value);
    }

    public String getRFC2253Value() {
        String typeOID  = type.getString();
        String typeName = ASN1OIDRegistry.lookupShortName(typeOID);
        if(typeName == null) {
            typeName = ASN1OIDRegistry.lookupName(typeOID);
        }
        if(typeName == null) {
            typeName = typeOID;
        }
        // TODO should encode as octetstring if type name not known
        return typeName + "=" + valueAsString();
    }

    private String valueAsString() {
        ASN1Object vo = value.getValue();
        String     vs = "<unknown>";
        if(vo instanceof DirectoryString) {
            vs = ((DirectoryString)vo).getString();
        } else if(vo instanceof ASN1CharString) {
            vs = ((ASN1CharString)vo).getValue();
        } else if(vo instanceof ASN1PrintableString) {
            vs = ((ASN1PrintableString)vo).getValue();
        } else if(vo instanceof ASN1BitString) {
            vs = ((ASN1BitString)vo).toPrintableString();
        } // else { TODO, encode as octetstring
        // TODO, escape characters according to rfc2253

        return vs;
    }

}

