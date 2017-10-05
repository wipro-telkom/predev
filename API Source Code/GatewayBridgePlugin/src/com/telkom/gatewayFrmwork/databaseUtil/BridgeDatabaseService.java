package com.telkom.gatewayFrmwork.databaseUtil;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Restrictions;

@SuppressWarnings("rawtypes")
public class BridgeDatabaseService {
	private Session session;

	public BridgeDatabaseService() {
		session = new AnnotationConfiguration().configure().buildSessionFactory().openSession();
	}

	public List getAll(Class arg) {
		return session.createCriteria(arg).list();
	}

	public void add(Object arg) {
		Transaction t = session.beginTransaction();
		session.persist(arg);
		t.commit();

	}

	public void delete(Object arg) {
		Transaction t = session.beginTransaction();
		session.delete(arg);
		t.commit();
	}

	public Object load(Class arg, int id) {
		return session.load(arg, id);
	}

	public void saveOrUpdate(Object obj) {
		Transaction t = session.beginTransaction();
		session.saveOrUpdate(obj);
		t.commit();
	}

	public List loadBykey(Class arg, String name, Object value,String role) {
		return session.createCriteria(arg).add(Restrictions.eq(name, value)).list();
	}

}
