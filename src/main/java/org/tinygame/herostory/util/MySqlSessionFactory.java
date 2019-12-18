package org.tinygame.herostory.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/17 19:35
 */
public final class MySqlSessionFactory {
  private static SqlSessionFactory factory;

  private MySqlSessionFactory() {
  }

  /**
   * 初始化SqlSessionFactory
   */
  public static void init() {
    try {
      InputStream in = Resources.getResourceAsStream("mybatisConfig.xml");
      factory = (new SqlSessionFactoryBuilder()).build(in);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 开启MySql会话
   * @return
   */
  public static SqlSession openSession() {
    if(factory==null) {
      throw new RuntimeException("SqlSessionFactory尚未初始化");
    }

    // 设置自动提交，不然没有提交
    return factory.openSession(true);
  }
}
