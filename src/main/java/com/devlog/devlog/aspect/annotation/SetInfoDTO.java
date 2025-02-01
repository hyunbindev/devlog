package com.devlog.devlog.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 이 어노테이션은 사용자 info 레이아이웃을 이용할 경우 DTO를 모델에 추가 시켜주는 annotation입니다.
 * 
 */


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SetInfoDTO {

}
