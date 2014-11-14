/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Note that $opt$ is a marker for the optimizing compiler to ensure
// it does compile the method.
public class Main {

  public static void assertByteEquals(byte expected, byte result) {
    if (expected != result) {
      throw new Error("Expected: " + expected + ", found: " + result);
    }
  }

  public static void assertShortEquals(short expected, short result) {
    if (expected != result) {
      throw new Error("Expected: " + expected + ", found: " + result);
    }
  }

  public static void assertIntEquals(int expected, int result) {
    if (expected != result) {
      throw new Error("Expected: " + expected + ", found: " + result);
    }
  }

  public static void assertLongEquals(long expected, long result) {
    if (expected != result) {
      throw new Error("Expected: " + expected + ", found: " + result);
    }
  }

  public static void assertCharEquals(char expected, char result) {
    if (expected != result) {
      // Values are cast to int to display numeric values instead of
      // (UTF-16 encoded) characters.
      throw new Error("Expected: " + (int)expected + ", found: " + (int)result);
    }
  }

  public static void main(String[] args) {
    // Generate, compile and check int-to-long Dex instructions.
    byteToLong();
    shortToLong();
    intToLong();
    charToLong();

    // Generate, compile and check long-to-int Dex instructions.
    longToInt();

    // Generate, compile and check int-to-byte Dex instructions.
    shortToByte();
    intToByte();
    charToByte();

    // Generate, compile and check int-to-short Dex instructions.
    byteToShort();
    intToShort();
    charToShort();

    // Generate, compile and check int-to-char Dex instructions.
    byteToChar();
    shortToChar();
    intToChar();
  }

  private static void byteToLong() {
    assertLongEquals(1L, $opt$ByteToLong((byte)1));
    assertLongEquals(0L, $opt$ByteToLong((byte)0));
    assertLongEquals(-1L, $opt$ByteToLong((byte)-1));
    assertLongEquals(51L, $opt$ByteToLong((byte)51));
    assertLongEquals(-51L, $opt$ByteToLong((byte)-51));
    assertLongEquals(127L, $opt$ByteToLong((byte)127));  // 2^7 - 1
    assertLongEquals(-127L, $opt$ByteToLong((byte)-127));  // -(2^7 - 1)
    assertLongEquals(-128L, $opt$ByteToLong((byte)-128));  // -(2^7)
  }

  private static void shortToLong() {
    assertLongEquals(1L, $opt$ShortToLong((short)1));
    assertLongEquals(0L, $opt$ShortToLong((short)0));
    assertLongEquals(-1L, $opt$ShortToLong((short)-1));
    assertLongEquals(51L, $opt$ShortToLong((short)51));
    assertLongEquals(-51L, $opt$ShortToLong((short)-51));
    assertLongEquals(32767L, $opt$ShortToLong((short)32767));  // 2^15 - 1
    assertLongEquals(-32767L, $opt$ShortToLong((short)-32767));  // -(2^15 - 1)
    assertLongEquals(-32768L, $opt$ShortToLong((short)-32768));  // -(2^15)
  }

  private static void intToLong() {
    assertLongEquals(1L, $opt$IntToLong(1));
    assertLongEquals(0L, $opt$IntToLong(0));
    assertLongEquals(-1L, $opt$IntToLong(-1));
    assertLongEquals(51L, $opt$IntToLong(51));
    assertLongEquals(-51L, $opt$IntToLong(-51));
    assertLongEquals(2147483647L, $opt$IntToLong(2147483647));  // 2^31 - 1
    assertLongEquals(-2147483647L, $opt$IntToLong(-2147483647));  // -(2^31 - 1)
    assertLongEquals(-2147483648L, $opt$IntToLong(-2147483648));  // -(2^31)
  }

  private static void charToLong() {
    assertLongEquals(1L, $opt$CharToLong((char)1));
    assertLongEquals(0L, $opt$CharToLong((char)0));
    assertLongEquals(51L, $opt$CharToLong((char)51));
    assertLongEquals(32767L, $opt$CharToLong((char)32767));  // 2^15 - 1
    assertLongEquals(65535L, $opt$CharToLong((char)65535));  // 2^16 - 1
    assertLongEquals(65535L, $opt$CharToLong((char)-1));
    assertLongEquals(65485L, $opt$CharToLong((char)-51));
    assertLongEquals(32769L, $opt$CharToLong((char)-32767));  // -(2^15 - 1)
    assertLongEquals(32768L, $opt$CharToLong((char)-32768));  // -(2^15)
  }

  private static void longToInt() {
    assertIntEquals(1, $opt$LongToInt(1L));
    assertIntEquals(0, $opt$LongToInt(0L));
    assertIntEquals(-1, $opt$LongToInt(-1L));
    assertIntEquals(51, $opt$LongToInt(51L));
    assertIntEquals(-51, $opt$LongToInt(-51L));
    assertIntEquals(2147483647, $opt$LongToInt(2147483647L));  // 2^31 - 1
    assertIntEquals(-2147483647, $opt$LongToInt(-2147483647L));  // -(2^31 - 1)
    assertIntEquals(-2147483648, $opt$LongToInt(-2147483648L));  // -(2^31)
    assertIntEquals(-2147483648, $opt$LongToInt(2147483648L));  // (2^31)
    assertIntEquals(2147483647, $opt$LongToInt(-2147483649L));  // -(2^31 + 1)
    assertIntEquals(-1, $opt$LongToInt(9223372036854775807L));  // 2^63 - 1
    assertIntEquals(1, $opt$LongToInt(-9223372036854775807L));  // -(2^63 - 1)
    assertIntEquals(0, $opt$LongToInt(-9223372036854775808L));  // -(2^63)

    assertIntEquals(42, $opt$LongLiteralToInt());

    // Ensure long-to-int conversions truncates values as expected.
    assertLongEquals(1L, $opt$IntToLong($opt$LongToInt(4294967297L)));  // 2^32 + 1
    assertLongEquals(0L, $opt$IntToLong($opt$LongToInt(4294967296L)));  // 2^32
    assertLongEquals(-1L, $opt$IntToLong($opt$LongToInt(4294967295L)));  // 2^32 - 1
    assertLongEquals(0L, $opt$IntToLong($opt$LongToInt(0L)));
    assertLongEquals(1L, $opt$IntToLong($opt$LongToInt(-4294967295L)));  // -(2^32 - 1)
    assertLongEquals(0L, $opt$IntToLong($opt$LongToInt(-4294967296L)));  // -(2^32)
    assertLongEquals(-1, $opt$IntToLong($opt$LongToInt(-4294967297L)));  // -(2^32 + 1)
  }

  private static void shortToByte() {
    assertByteEquals((byte)1, $opt$ShortToByte((short)1));
    assertByteEquals((byte)0, $opt$ShortToByte((short)0));
    assertByteEquals((byte)-1, $opt$ShortToByte((short)-1));
    assertByteEquals((byte)51, $opt$ShortToByte((short)51));
    assertByteEquals((byte)-51, $opt$ShortToByte((short)-51));
    assertByteEquals((byte)127, $opt$ShortToByte((short)127));  // 2^7 - 1
    assertByteEquals((byte)-127, $opt$ShortToByte((short)-127));  // -(2^7 - 1)
    assertByteEquals((byte)-128, $opt$ShortToByte((short)-128));  // -(2^7)
    assertByteEquals((byte)-128, $opt$ShortToByte((short)128));  // 2^7
    assertByteEquals((byte)127, $opt$ShortToByte((short)-129));  // -(2^7 + 1)
    assertByteEquals((byte)-1, $opt$ShortToByte((short)32767));  // 2^15 - 1
    assertByteEquals((byte)0, $opt$ShortToByte((short)-32768));  // -(2^15)
  }

  private static void intToByte() {
    assertByteEquals((byte)1, $opt$IntToByte(1));
    assertByteEquals((byte)0, $opt$IntToByte(0));
    assertByteEquals((byte)-1, $opt$IntToByte(-1));
    assertByteEquals((byte)51, $opt$IntToByte(51));
    assertByteEquals((byte)-51, $opt$IntToByte(-51));
    assertByteEquals((byte)127, $opt$IntToByte(127));  // 2^7 - 1
    assertByteEquals((byte)-127, $opt$IntToByte(-127));  // -(2^7 - 1)
    assertByteEquals((byte)-128, $opt$IntToByte(-128));  // -(2^7)
    assertByteEquals((byte)-128, $opt$IntToByte(128));  // 2^7
    assertByteEquals((byte)127, $opt$IntToByte(-129));  // -(2^7 + 1)
    assertByteEquals((byte)-1, $opt$IntToByte(2147483647));  // 2^31 - 1
    assertByteEquals((byte)0, $opt$IntToByte(-2147483648));  // -(2^31)
  }

  private static void charToByte() {
    assertByteEquals((byte)1, $opt$CharToByte((char)1));
    assertByteEquals((byte)0, $opt$CharToByte((char)0));
    assertByteEquals((byte)51, $opt$CharToByte((char)51));
    assertByteEquals((byte)127, $opt$CharToByte((char)127));  // 2^7 - 1
    assertByteEquals((byte)-128, $opt$CharToByte((char)128));  // 2^7
    assertByteEquals((byte)-1, $opt$CharToByte((char)32767));  // 2^15 - 1
    assertByteEquals((byte)-1, $opt$CharToByte((char)65535));  // 2^16 - 1
    assertByteEquals((byte)-1, $opt$CharToByte((char)-1));
    assertByteEquals((byte)-51, $opt$CharToByte((char)-51));
    assertByteEquals((byte)-127, $opt$CharToByte((char)-127));  // -(2^7 - 1)
    assertByteEquals((byte)-128, $opt$CharToByte((char)-128));  // -(2^7)
    assertByteEquals((byte)127, $opt$CharToByte((char)-129));  // -(2^7 + 1)
  }

  private static void byteToShort() {
    assertShortEquals((short)1, $opt$ByteToShort((byte)1));
    assertShortEquals((short)0, $opt$ByteToShort((byte)0));
    assertShortEquals((short)-1, $opt$ByteToShort((byte)-1));
    assertShortEquals((short)51, $opt$ByteToShort((byte)51));
    assertShortEquals((short)-51, $opt$ByteToShort((byte)-51));
    assertShortEquals((short)127, $opt$ByteToShort((byte)127));  // 2^7 - 1
    assertShortEquals((short)-127, $opt$ByteToShort((byte)-127));  // -(2^7 - 1)
    assertShortEquals((short)-128, $opt$ByteToShort((byte)-128));  // -(2^7)
  }

  private static void intToShort() {
    assertShortEquals((short)1, $opt$IntToShort(1));
    assertShortEquals((short)0, $opt$IntToShort(0));
    assertShortEquals((short)-1, $opt$IntToShort(-1));
    assertShortEquals((short)51, $opt$IntToShort(51));
    assertShortEquals((short)-51, $opt$IntToShort(-51));
    assertShortEquals((short)32767, $opt$IntToShort(32767));  // 2^15 - 1
    assertShortEquals((short)-32767, $opt$IntToShort(-32767));  // -(2^15 - 1)
    assertShortEquals((short)-32768, $opt$IntToShort(-32768));  // -(2^15)
    assertShortEquals((short)-32768, $opt$IntToShort(32768));  // 2^15
    assertShortEquals((short)32767, $opt$IntToShort(-32769));  // -(2^15 + 1)
    assertShortEquals((short)-1, $opt$IntToShort(2147483647));  // 2^31 - 1
    assertShortEquals((short)0, $opt$IntToShort(-2147483648));  // -(2^31)
  }

  private static void charToShort() {
    assertShortEquals((short)1, $opt$CharToShort((char)1));
    assertShortEquals((short)0, $opt$CharToShort((char)0));
    assertShortEquals((short)51, $opt$CharToShort((char)51));
    assertShortEquals((short)32767, $opt$CharToShort((char)32767));  // 2^15 - 1
    assertShortEquals((short)-32768, $opt$CharToShort((char)32768));  // 2^15
    assertShortEquals((short)-32767, $opt$CharToShort((char)32769));  // 2^15
    assertShortEquals((short)-1, $opt$CharToShort((char)65535));  // 2^16 - 1
    assertShortEquals((short)-1, $opt$CharToShort((char)-1));
    assertShortEquals((short)-51, $opt$CharToShort((char)-51));
    assertShortEquals((short)-32767, $opt$CharToShort((char)-32767));  // -(2^15 - 1)
    assertShortEquals((short)-32768, $opt$CharToShort((char)-32768));  // -(2^15)
    assertShortEquals((short)32767, $opt$CharToShort((char)-32769));  // -(2^15 + 1)
  }

  private static void byteToChar() {
    assertCharEquals((char)1, $opt$ByteToChar((byte)1));
    assertCharEquals((char)0, $opt$ByteToChar((byte)0));
    assertCharEquals((char)65535, $opt$ByteToChar((byte)-1));
    assertCharEquals((char)51, $opt$ByteToChar((byte)51));
    assertCharEquals((char)65485, $opt$ByteToChar((byte)-51));
    assertCharEquals((char)127, $opt$ByteToChar((byte)127));  // 2^7 - 1
    assertCharEquals((char)65409, $opt$ByteToChar((byte)-127));  // -(2^7 - 1)
    assertCharEquals((char)65408, $opt$ByteToChar((byte)-128));  // -(2^7)
  }

  private static void shortToChar() {
    assertCharEquals((char)1, $opt$ShortToChar((short)1));
    assertCharEquals((char)0, $opt$ShortToChar((short)0));
    assertCharEquals((char)65535, $opt$ShortToChar((short)-1));
    assertCharEquals((char)51, $opt$ShortToChar((short)51));
    assertCharEquals((char)65485, $opt$ShortToChar((short)-51));
    assertCharEquals((char)32767, $opt$ShortToChar((short)32767));  // 2^15 - 1
    assertCharEquals((char)32769, $opt$ShortToChar((short)-32767));  // -(2^15 - 1)
    assertCharEquals((char)32768, $opt$ShortToChar((short)-32768));  // -(2^15)
  }

  private static void intToChar() {
    assertCharEquals((char)1, $opt$IntToChar(1));
    assertCharEquals((char)0, $opt$IntToChar(0));
    assertCharEquals((char)65535, $opt$IntToChar(-1));
    assertCharEquals((char)51, $opt$IntToChar(51));
    assertCharEquals((char)65485, $opt$IntToChar(-51));
    assertCharEquals((char)32767, $opt$IntToChar(32767));  // 2^15 - 1
    assertCharEquals((char)32769, $opt$IntToChar(-32767));  // -(2^15 - 1)
    assertCharEquals((char)32768, $opt$IntToChar(32768));  // 2^15
    assertCharEquals((char)32768, $opt$IntToChar(-32768));  // -(2^15)
    assertCharEquals((char)65535, $opt$IntToChar(65535));  // 2^16 - 1
    assertCharEquals((char)1, $opt$IntToChar(-65535));  // -(2^16 - 1)
    assertCharEquals((char)0, $opt$IntToChar(65536));  // 2^16
    assertCharEquals((char)0, $opt$IntToChar(-65536));  // -(2^16)
    assertCharEquals((char)65535, $opt$IntToChar(2147483647));  // 2^31 - 1
    assertCharEquals((char)0, $opt$IntToChar(-2147483648));  // -(2^31)
  }


  // These methods produce int-to-long Dex instructions.
  static long $opt$ByteToLong(byte a) { return a; }
  static long $opt$ShortToLong(short a) { return a; }
  static long $opt$IntToLong(int a) { return a; }
  static long $opt$CharToLong(int a) { return a; }

  // These methods produce long-to-int Dex instructions.
  static int $opt$LongToInt(long a){ return (int)a; }
  static int $opt$LongLiteralToInt(){ return (int)42L; }

  // These methods produce int-to-byte Dex instructions.
  static byte $opt$ShortToByte(short a){ return (byte)a; }
  static byte $opt$IntToByte(int a){ return (byte)a; }
  static byte $opt$CharToByte(char a){ return (byte)a; }

  // These methods produce int-to-short Dex instructions.
  static short $opt$ByteToShort(byte a){ return (short)a; }
  static short $opt$IntToShort(int a){ return (short)a; }
  static short $opt$CharToShort(char a){ return (short)a; }

  // These methods produce int-to-char Dex instructions.
  static char $opt$ByteToChar(byte a){ return (char)a; }
  static char $opt$ShortToChar(short a){ return (char)a; }
  static char $opt$IntToChar(int a){ return (char)a; }
}
