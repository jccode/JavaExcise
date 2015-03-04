package tk.jcchen.excise.proxytest;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CglibWave {
	
	public static void main(String[] args) {
		Salary s = new Salary();
		Advice ca = new ControlAdvice();
		SalaryFacadeCglib cglib = new SalaryFacadeCglib();
		Salary cgs = (Salary) cglib.bind(s, ca);
		cgs.doSalary();
	}
}

class SalaryFacadeCglib implements MethodInterceptor {
	Object object;
	Advice advice;
	
	public Object bind(Object o, Advice advice) {
		this.object = o;
		this.advice = advice;
		
		// create enhance subclass of target object
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(this.object.getClass());
		enhancer.setCallback(this);
		return enhancer.create();
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Object result = null;
		advice.before();
		// invoke super class method
		result = proxy.invokeSuper(obj, args);
		advice.after();
		return result;
	}

}
