package tk.jcchen.excise.ex1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy {
	public static void main(String[] args) {
		withAdvice();
	}

	static void original() {
		ISalary s = new Salary();
		s.doSalary();
	}

	static void withAdvice() {
		AdviceProxy proxy = new AdviceProxy();
		ISalary s = new Salary();
		Advice ca = new ControlAdvice();
		ISalary ps = (ISalary) proxy.bind(s, ca);
		ps.doSalary();
	}
}

// advice
interface Advice {
	void before();

	void after();
}

// advice implements
class TimeAdvice implements Advice {
	long startTime;
	long endTime;

	public void before() {
		startTime = System.nanoTime();
		System.out.println("[Advice] Record start time.");
	}

	public void after() {
		endTime = System.nanoTime();
		System.out.println("[Advice] " + (endTime - startTime) + " ns elapsed");
	}
}

class ControlAdvice extends TimeAdvice {

	public void before() {
		super.before();
		System.out.println("[Advice] Permisson judgement.");
	}

	public void after() {
		super.after();
		System.out.println("[Advice] Complete permission judgement");
	}

}

// bussiness
interface ISalary {
	void doSalary();
}

class Salary implements ISalary {

	public void doSalary() {
		System.out.println("Do salary.");
	}

}

// dynamic proxy
class AdviceProxy implements InvocationHandler {

	Object object;
	Advice advice;

	public Object bind(Object o, Advice advice) {
		this.object = o;
		this.advice = advice;
		return Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), this);
	}
	
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = null;
		advice.before();
		result = method.invoke(object, args);
		advice.after();
		return result;
	}

}
