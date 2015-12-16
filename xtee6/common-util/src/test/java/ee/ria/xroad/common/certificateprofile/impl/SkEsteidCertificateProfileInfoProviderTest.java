package ee.ria.xroad.common.certificateprofile.impl;

import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import org.junit.Test;
import org.mockito.Mockito;

import ee.ria.xroad.common.certificateprofile.SignCertificateProfileInfo;
import ee.ria.xroad.common.identifier.ClientId;

import static org.junit.Assert.assertEquals;

/**
 * Tests the implementation of SkEsteidCertificateProfileInfoProvider.
 */
public class SkEsteidCertificateProfileInfoProviderTest {

    /**
     * Tests whether getting subject identifier succeeds as expected.
     * @throws Exception in case of any unexpected errors
     */
    @Test
    public void getSubjectIdentifier() throws Exception {
        assertEquals(
            ClientId.create("XX", "PERSON", "foobar"),
            id("SERIALNUMBER=foobar")
        );
    }

    /**
     * Tests whether getting subject identifier fails if serial number of
     * the certificate is missing.
     * @throws Exception in case of any unexpected errors
     */
    @Test(expected = Exception.class)
    public void missingSerialNumber() throws Exception {
        id("C=x");
    }

    private ClientId id(String name) throws Exception {
        X509Certificate mockCert = Mockito.mock(X509Certificate.class);

        Mockito.when(mockCert.getSubjectX500Principal()).thenReturn(
            new X500Principal(name)
        );

        return new SkEsteIdCertificateProfileInfoProvider().getSignCertProfile(
            new SignCertificateProfileInfo.Parameters() {
                @Override
                public ClientId getClientId() {
                    return ClientId.create("XX", "foo", "bar");
                }
                @Override
                public String getMemberName() {
                    return "foo";
                }
            }
        ).getSubjectIdentifier(mockCert);
    }
}
