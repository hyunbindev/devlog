package com.devlog.devlog.exception;

import io.jsonwebtoken.ExpiredJwtException;

public class JwtCustomException {
	public static class TokenExpiredException extends RuntimeException{
		public TokenExpiredException(Throwable cause) {
			super("Token is expired",cause);
		}
	}
	public static class TokenInvalidException extends RuntimeException{
		public TokenInvalidException(Throwable cause) {
			super("Invalid token",cause);
		}
	}
	public static class TokenUnexpectedException extends RuntimeException{
		public TokenUnexpectedException(Throwable cause) {
			super("Unexpected exception",cause);
		}
	}
	public static class NullTokenException extends RuntimeException{
		public NullTokenException() {
			super("NullTokenException");
		}
	}
	public static Exception NullTokenException() {
		// TODO Auto-generated method stub
		return null;
	}
}
