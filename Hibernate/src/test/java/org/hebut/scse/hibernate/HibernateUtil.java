package org.hebut.scse.hibernate;

import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Created by jxy on 2016/12/8.
 */
public class HibernateUtil {
    public static Configuration conf = new Configuration().configure("/config/hibernate.cfg.xml");
}
