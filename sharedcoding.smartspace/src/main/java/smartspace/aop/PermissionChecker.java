package smartspace.aop;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.ActionType;


@Component
@Aspect
public class PermissionChecker {
	Log logger = LogFactory.getLog(CheckRoleOfUser.class);
	private EnhancedUserDao<String> userDao;
	
	@Autowired
	public PermissionChecker(EnhancedUserDao<String> userDao) {
		super();
		this.userDao = userDao;
	}
	
	
	@Around("@annotation(smartspace.aop.CheckRoleOfUser) && args(userSmartspace,userEmail,role,..)")
	public Object checkRoll(ProceedingJoinPoint pjp, String userSmartspace, String userEmail, ActionType role) throws Throwable {
		// before
		String method = pjp.getSignature().getName();
		String fullyQualifiedClassName = pjp.getTarget().getClass().getName();
		Object[] args = pjp.getArgs();
		Optional<UserEntity> user = this.userDao.readById(userSmartspace + "=" + userEmail);

//		if(user.isPresent())
//			args[2] = user.get().getRole();
//		else
//			throw new RuntimeException("The user isn't exist");
				
		logger.debug("********* " + fullyQualifiedClassName + "." + method + "(" + userSmartspace + ","+ userEmail + ","+ role + ",.....)" + role);
		
		return pjp.proceed(args);	
	}	
}

