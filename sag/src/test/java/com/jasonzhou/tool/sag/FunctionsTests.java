package com.jasonzhou.tool.sag;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.jasonzhou.tool.sag.func.Functions;

@SpringBootTest
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class FunctionsTests {

	@Test
	void testArg0() {
		String express = "test()";
		String funcName = Functions.parseFuncName(express);
		Object[] args = Functions.parseArgs(express);
		assertEquals(funcName, "test");
		assertEquals(args.length, 0);
	}

	@Test
	void testArg1Int() {
		String express = "test(1)";
		String funcName = Functions.parseFuncName(express);
		Object[] args = Functions.parseArgs(express);
		assertEquals(funcName, "test");
		assertEquals(args.length, 1);
		assertEquals(args[0], 1);
	}
	@Test
	void testArg1String() {
		String express = "test('abc')";
		String funcName = Functions.parseFuncName(express);
		Object[] args = Functions.parseArgs(express);
		assertEquals(funcName, "test");
		assertEquals(args.length, 1);
		assertEquals(args[0], "abc");
	}

	@Test
	void testArg2String() {
		String express = "test('abc', 'cba')";
		String funcName = Functions.parseFuncName(express);
		Object[] args = Functions.parseArgs(express);
		assertEquals(funcName, "test");
		assertEquals(args.length, 2);
		assertEquals(args[0], "abc");
		assertEquals(args[1], "cba");
	}
	@Test
	void testArg2StringInt() {
		String express = "test('abc', 123)";
		String funcName = Functions.parseFuncName(express);
		Object[] args = Functions.parseArgs(express);
		assertEquals(funcName, "test");
		assertEquals(args.length, 2);
		assertEquals(args[0], "abc");
		assertEquals(args[1], 123);
	}
	@Test
	void testArg2IntString() {
		String express = "test(123, 'abc')";
		String funcName = Functions.parseFuncName(express);
		Object[] args = Functions.parseArgs(express);
		assertEquals(funcName, "test");
		assertEquals(args.length, 2);
		assertEquals(args[0], 123);
		assertEquals(args[1], "abc");
	}

	@Test
	void testArg3IntStringInt() {
		String express = "test(123, 'abc' , 321)";
		String funcName = Functions.parseFuncName(express);
		Object[] args = Functions.parseArgs(express);
		assertEquals(funcName, "test");
		assertEquals(args.length, 3);
		assertEquals(args[0], 123);
		assertEquals(args[1], "abc");
		assertEquals(args[2], 321);
	}
	
	@Test
	void testArg3StringIntString() {
		String express = "test('abc' , 123, 'cba'     )";
		String funcName = Functions.parseFuncName(express);
		Object[] args = Functions.parseArgs(express);
		assertEquals(funcName, "test");
		assertEquals(args.length, 3);
		assertEquals(args[0], "abc");
		assertEquals(args[1], 123);
		assertEquals(args[2], "cba");
	}
}
