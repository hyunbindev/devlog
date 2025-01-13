package com.devlog.devlog.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
*이 어노테이션은 접근하려는 자원과 사용자의 권환을 비교하여 model에 담는 어노테이션입니다.
* ex) controllerMethod(Authentication authentication , String memberId ,Model model..) 순으로 입력되어야합니다.
* model 에 isOwner attribute가 추가됩니다.
 */


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckOwner {
	
}
