package rs.emulate.util.crypto.rsa

import org.bouncycastle.asn1.DERNull
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.asn1.pkcs.RSAPrivateKey
import org.bouncycastle.asn1.pkcs.RSAPublicKey
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.crypto.params.RSAKeyParameters
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters
import org.bouncycastle.util.io.pem.PemReader
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

class RsaKeyReader {
    fun readPrivate(path: Path): RSAKeyParameters {
        PemReader(Files.newBufferedReader(path)).use { reader ->
            val pemObject = reader.readPemObject()
            if (pemObject == null || pemObject.type != "PRIVATE KEY") {
                throw IOException("Expecting PRIVATE KEY")
            }

            if (pemObject.headers.isNotEmpty()) {
                throw IOException("Not expecting PEM headers")
            }

            if (reader.readPemObject() != null) {
                throw IOException("Not expecting trailing PEM object")
            }

            val pki = PrivateKeyInfo.getInstance(pemObject.content)

            val id = pki.privateKeyAlgorithm
            if (id.algorithm != PKCSObjectIdentifiers.rsaEncryption) {
                throw IOException("Expecting rsaEncryption")
            }

            if (id.parameters != DERNull.INSTANCE) {
                throw IOException("Expecting NULL")
            }

            val key = RSAPrivateKey.getInstance(pki.parsePrivateKey())
            return RSAPrivateCrtKeyParameters(
                key.modulus,
                key.publicExponent,
                key.privateExponent,
                key.prime1,
                key.prime2,
                key.exponent1,
                key.exponent2,
                key.coefficient
            )
        }
    }

    fun readPublic(path: Path): RSAKeyParameters {
        PemReader(Files.newBufferedReader(path)).use { reader ->
            val pemObject = reader.readPemObject()
            if (pemObject == null || pemObject.type != "PUBLIC KEY") {
                throw IOException("Expecting PUBLIC KEY")
            }

            if (pemObject.headers.isNotEmpty()) {
                throw IOException("Not expecting PEM headers")
            }

            if (reader.readPemObject() != null) {
                throw IOException("Not expecting trailing PEM object")
            }

            val spki = SubjectPublicKeyInfo.getInstance(pemObject.content)

            val id = spki.algorithm
            if (id.algorithm != PKCSObjectIdentifiers.rsaEncryption) {
                throw IOException("Expecting rsaEncryption")
            }

            if (id.parameters != DERNull.INSTANCE) {
                throw IOException("Expecting NULL")
            }

            val key = RSAPublicKey.getInstance(spki.parsePublicKey())
            return RSAKeyParameters(false, key.modulus, key.publicExponent)
        }
    }
}
