//package com.shoppingmall.db;
//
//import com.nimbusds.openid.connect.sdk.claims.UserInfo;
//import com.shoppingmall.domain.entity.MemberEntity;
//import com.shoppingmall.vo.Member;
//import org.apache.ibatis.session.SqlSession;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.orm.jpa.EntityManagerFactoryUtils;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.support.TransactionTemplate;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import java.util.List;
//
//@SpringBootTest
//public class JPAWithMybatisApplicationTest {
//
//    @Autowired
//    private EntityManagerFactory entityManagerFactory;
//
//    @Autowired
//    private SqlSession sqlSession;
//
//    @Autowired
//    private PlatformTransactionManager transactionManager;
//
//    @Test
//    public void contextLoad() {
//        // transaction 단위
//        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
//        transactionTemplate.execute( transactionStatus -> {
//            EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
//            MemberEntity memberEntity = MemberEntity.builder()
//                    .userName("이름")
//                    .account("test01")
//                    .build();
//
//            MemberEntity member = em.find(MemberEntity.class);
//            List<Object> objects = sqlSession.selectList("h2.selectUserInfo", null);
//            System.out.println("In transaction : " + objects.size());
//            return userInfo;
//        });
//
//        List<Object> objects = sqlSession.selectList("h2.selectUserInfo", null);
//        System.out.println("Out transaction : " + objects.size());
//    }
//}
