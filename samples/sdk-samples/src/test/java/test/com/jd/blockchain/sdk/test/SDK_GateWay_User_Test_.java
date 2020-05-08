/**
 * Copyright: Copyright 2016-2020 JD.COM All Right Reserved
 * FileName: test.com.jd.blockchain.sdk.test.SDK_GateWay_InsertData_Test
 * Author: shaozhuguang
 * Department: 区块链研发部
 * Date: 2018/9/4 上午11:06
 * Description: 插入数据测试
 */
package test.com.jd.blockchain.sdk.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.crypto.AsymmetricKeypair;
import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.HashFunction;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.SignatureFunction;
import com.jd.blockchain.ledger.BlockchainKeyGenerator;
import com.jd.blockchain.ledger.BlockchainKeypair;
import com.jd.blockchain.ledger.EndpointRequest;
import com.jd.blockchain.ledger.NodeRequest;
import com.jd.blockchain.ledger.PreparedTransaction;
import com.jd.blockchain.ledger.TransactionContent;
import com.jd.blockchain.ledger.TransactionContentBody;
import com.jd.blockchain.ledger.TransactionRequest;
import com.jd.blockchain.ledger.TransactionResponse;
import com.jd.blockchain.ledger.TransactionState;
import com.jd.blockchain.ledger.TransactionTemplate;
import com.jd.blockchain.sdk.BlockchainService;
import com.jd.blockchain.sdk.client.GatewayServiceFactory;
import com.jd.blockchain.transaction.TxResponseMessage;

/**
 * 插入数据测试
 * @author shaozhuguang
 * @create 2018/9/4
 * @since 1.0.0
 */

public class SDK_GateWay_User_Test_ {

//    public static final String PASSWORD = SDK_GateWay_KeyPair_Para.PASSWORD;
//
//    public static final String[] PUB_KEYS = SDK_GateWay_KeyPair_Para.PUB_KEYS;
//
//    public static final String[] PRIV_KEYS = SDK_GateWay_KeyPair_Para.PRIV_KEYS;

    private PrivKey privKey;
    private PubKey pubKey;

    private BlockchainKeypair CLIENT_CERT = null;

    private String GATEWAY_IPADDR = null;

    private int GATEWAY_PORT;

    private boolean SECURE;

    private BlockchainService service;

    @Before
    public void init() {

//        PrivKey privkey0 = KeyGenCommand.decodePrivKeyWithRawPassword(PRIV_KEYS[0], PASSWORD);
//        PrivKey privkey1 = KeyGenCommand.decodePrivKeyWithRawPassword(PRIV_KEYS[1], PASSWORD);
//        PrivKey privkey2 = KeyGenCommand.decodePrivKeyWithRawPassword(PRIV_KEYS[2], PASSWORD);
//        PrivKey privkey3 = KeyGenCommand.decodePrivKeyWithRawPassword(PRIV_KEYS[3], PASSWORD);
//
//        PubKey pubKey0 = KeyGenCommand.decodePubKey(PUB_KEYS[0]);
//        PubKey pubKey1 = KeyGenCommand.decodePubKey(PUB_KEYS[1]);
//        PubKey pubKey2 = KeyGenCommand.decodePubKey(PUB_KEYS[2]);
//        PubKey pubKey3 = KeyGenCommand.decodePubKey(PUB_KEYS[3]);

        privKey = SDK_GateWay_KeyPair_Para.privkey1;
        pubKey = SDK_GateWay_KeyPair_Para.pubKey1;

        CLIENT_CERT = new BlockchainKeypair(SDK_GateWay_KeyPair_Para.pubKey0, SDK_GateWay_KeyPair_Para.privkey0);
        GATEWAY_IPADDR = "127.0.0.1";
        GATEWAY_PORT = 8081;
        SECURE = false;
        GatewayServiceFactory serviceFactory = GatewayServiceFactory.connect(GATEWAY_IPADDR, GATEWAY_PORT, SECURE,
                CLIENT_CERT);
        service = serviceFactory.getBlockchainService();

        DataContractRegistry.register(TransactionContent.class);
        DataContractRegistry.register(TransactionContentBody.class);
        DataContractRegistry.register(TransactionRequest.class);
        DataContractRegistry.register(NodeRequest.class);
        DataContractRegistry.register(EndpointRequest.class);
        DataContractRegistry.register(TransactionResponse.class);
    }

    @Test
    public void registerUser_Test() {
        HashDigest[] ledgerHashs = service.getLedgerHashs();
        // 在本地定义注册账号的 TX；
        TransactionTemplate txTemp = service.newTransaction(ledgerHashs[0]);

        //existed signer
        AsymmetricKeypair keyPair = new BlockchainKeypair(pubKey, privKey);

        BlockchainKeypair user = BlockchainKeyGenerator.getInstance().generate();

        // 注册
        txTemp.users().register(user.getIdentity());

        // TX 准备就绪；
        PreparedTransaction prepTx = txTemp.prepare();

        // 使用私钥进行签名；
        prepTx.sign(keyPair);

        // 提交交易；
        TransactionResponse transactionResponse = prepTx.commit();
        assertTrue(transactionResponse.isSuccess());

        // 期望返回结果
//       TransactionResponse expectResp = initResponse();
//
//          System.out.println("---------- assert start ----------");
//        assertEquals(expectResp.isSuccess(), transactionResponse.isSuccess());
//        assertEquals(expectResp.getExecutionState(), transactionResponse.getExecutionState());
//        assertEquals(expectResp.getContentHash(), transactionResponse.getContentHash());
//        assertEquals(expectResp.getBlockHeight(), transactionResponse.getBlockHeight());
//        assertEquals(expectResp.getBlockHash(), transactionResponse.getBlockHash());
//        System.out.println("---------- assert OK ----------");
    }

//    private HashDigest getLedgerHash() {
//        byte[] hashBytes = Base58Utils.decode(ledgerHashBase58);
//        return new HashDigest(hashBytes);
//    }

    private AsymmetricKeypair getSponsorKey() {
        SignatureFunction signatureFunction = Crypto.getSignatureFunction("ED25519");
        return signatureFunction.generateKeypair();
    }

    private TransactionResponse initResponse() {
    	HashFunction hashFunc = Crypto.getHashFunction("SHA256");;
        HashDigest contentHash = hashFunc.hash("contentHash".getBytes());
        HashDigest blockHash =  hashFunc.hash("blockHash".getBytes());
        long blockHeight = 9998L;

        TxResponseMessage resp = new TxResponseMessage(contentHash);
        resp.setBlockHash(blockHash);
        resp.setBlockHeight(blockHeight);
        resp.setExecutionState(TransactionState.SUCCESS);
        return resp;
    }
}