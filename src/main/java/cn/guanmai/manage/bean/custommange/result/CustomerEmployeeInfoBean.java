package cn.guanmai.manage.bean.custommange.result;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author liming
 * @date 2019年8月21日
 * @time 上午11:23:42
 * @des 接口 /custommanage/saleemployee/simple_info 对应的结果
 */

public class CustomerEmployeeInfoBean {
	private List<Role> roles;

	@JSONField(name="sale_employees")
	private List<Employee> employees;

	public class Role {
		private BigDecimal id;
		private String name;

		public BigDecimal getId() {
			return id;
		}

		public void setId(BigDecimal id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public class Employee {
		private BigDecimal id;
		private String name;
		private List<BigDecimal> role_ids;

		public BigDecimal getId() {
			return id;
		}

		public void setId(BigDecimal id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<BigDecimal> getRole_ids() {
			return role_ids;
		}

		public void setRole_ids(List<BigDecimal> role_ids) {
			this.role_ids = role_ids;
		}
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

}
