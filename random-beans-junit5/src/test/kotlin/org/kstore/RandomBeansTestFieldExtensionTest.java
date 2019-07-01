package org.kstore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Strings.isNullOrEmpty;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;

@Extensions(@ExtendWith(RandomBeansExtension.class))
class RandomBeansTestFieldExtensionTest {

    @RandomBean
    private String string;
    @RandomBean
    private TestData data;
    @RandomBean
    private List<TestData2> list;
    @RandomBean
    private Set<TestData2> set;
    @RandomBean
    private Queue<TestData2> queue;
    @RandomBean
    private Map<TestKey, TestData2> map;

    @Test
    void randomBeanGenerateStringParameter() {
        System.out.println(string);
        assertThat(string).isNotBlank();
    }

    @SuppressWarnings("Duplicates")
    @Test
    void randomBeanGenerateBeanParameter() {
        System.out.println(data);
        assertThat(data.string).isNotBlank();
        assertThat(data.bigDecimal).isNotNull();

        assertThat(data.byteObject).isNotNull();
        assertThat(data.shortObject).isNotNull();
        assertThat(data.integerObject).isNotNull();
        assertThat(data.longObject).isNotNull();
        assertThat(data.floatObject).isNotNull();
        assertThat(data.doubleObject).isNotNull();
        assertThat(data.booleanObject).isNotNull();
        assertThat(data.charObject).isNotNull();

        assertThat(data.bytePrimitive).isNotNull();
        assertThat(data.shortPrimitive).isNotNull();
        assertThat(data.intPrimitive).isNotNull();
        assertThat(data.longPrimitive).isNotNull();
        assertThat(data.floatPrimitive).isNotNull();
        assertThat(data.doublePrimitive).isNotNull();
        assertThat(data.booleanPrimitive).isNotNull();
        assertThat(data.charPrimitive).isNotNull();

        assertThat(data.byteObjectArray).isNotEmpty();
        assertThat(data.shortObjectArray).isNotEmpty();
        assertThat(data.integerObjectArray).isNotEmpty();
        assertThat(data.longObjectArray).isNotEmpty();
        assertThat(data.floatObjectArray).isNotEmpty();
        assertThat(data.doubleObjectArray).isNotEmpty();
        assertThat(data.booleanObjectArray).isNotEmpty();
        assertThat(data.charObjectArray).isNotEmpty();

        assertThat(data.bytePrimitiveArray).isNotEmpty();
        assertThat(data.shortPrimitiveArray).isNotEmpty();
        assertThat(data.intPrimitiveArray).isNotEmpty();
        assertThat(data.longPrimitiveArray).isNotEmpty();
        assertThat(data.floatPrimitiveArray).isNotEmpty();
        assertThat(data.doublePrimitiveArray).isNotEmpty();
        assertThat(data.booleanPrimitiveArray).isNotEmpty();
        assertThat(data.charPrimitiveArray).isNotEmpty();
    }

    @Test
    void randomBeanGenerateGenericListBeanParameter() {
        System.out.println(list);
        assertThat(list).isNotEmpty()
                .allMatch(testData2 -> !isNullOrEmpty(testData2.string));
    }

    @Test
    void randomBeanGenerateGenericSetBeanParameter() {
        System.out.println(set);
        assertThat(set).isNotEmpty()
                .allMatch(testData2 -> !isNullOrEmpty(testData2.string));
    }

    @Test
    void randomBeanGenerateGenericQueueBeanParameter() {
        System.out.println(queue);
        assertThat(queue).isNotEmpty()
                .allMatch(testData2 -> !isNullOrEmpty(testData2.string));
    }

    @Test
    void randomBeanGenerateGenericMapBeanParameter() {
        System.out.println(map);
        assertThat(map).isNotEmpty();
        assertThat(map.values()).allMatch(value -> !isNullOrEmpty(value.string));
        assertThat(map.keySet()).allMatch(key -> !isNullOrEmpty(key.string));
    }

    private static class TestData {

        private String string;
        private BigDecimal bigDecimal;

        private Byte byteObject;
        private Short shortObject;
        private Integer integerObject;
        private Long longObject;
        private Float floatObject;
        private Double doubleObject;
        private Boolean booleanObject;
        private Character charObject;

        private byte bytePrimitive;
        private short shortPrimitive;
        private int intPrimitive;
        private long longPrimitive;
        private float floatPrimitive;
        private double doublePrimitive;
        private boolean booleanPrimitive;
        private char charPrimitive;

        private Byte[] byteObjectArray;
        private Short[] shortObjectArray;
        private Integer[] integerObjectArray;
        private Long[] longObjectArray;
        private Float[] floatObjectArray;
        private Double[] doubleObjectArray;
        private Boolean[] booleanObjectArray;
        private Character[] charObjectArray;

        private byte[] bytePrimitiveArray;
        private short[] shortPrimitiveArray;
        private int[] intPrimitiveArray;
        private long[] longPrimitiveArray;
        private float[] floatPrimitiveArray;
        private double[] doublePrimitiveArray;
        private boolean[] booleanPrimitiveArray;
        private char[] charPrimitiveArray;

        @Override
        public String toString() {
            return "TestData{" +
                    "string='" + string + '\'' +
                    ", bigDecimal=" + bigDecimal +
                    ", byteObject=" + byteObject +
                    ", shortObject=" + shortObject +
                    ", integerObject=" + integerObject +
                    ", longObject=" + longObject +
                    ", floatObject=" + floatObject +
                    ", doubleObject=" + doubleObject +
                    ", booleanObject=" + booleanObject +
                    ", charObject=" + charObject +
                    ", bytePrimitive=" + bytePrimitive +
                    ", shortPrimitive=" + shortPrimitive +
                    ", intPrimitive=" + intPrimitive +
                    ", longPrimitive=" + longPrimitive +
                    ", floatPrimitive=" + floatPrimitive +
                    ", doublePrimitive=" + doublePrimitive +
                    ", booleanPrimitive=" + booleanPrimitive +
                    ", charPrimitive=" + charPrimitive +
                    ", byteObjectArray=" + Arrays.toString(byteObjectArray) +
                    ", shortObjectArray=" + Arrays.toString(shortObjectArray) +
                    ", integerObjectArray=" + Arrays.toString(integerObjectArray) +
                    ", longObjectArray=" + Arrays.toString(longObjectArray) +
                    ", floatObjectArray=" + Arrays.toString(floatObjectArray) +
                    ", doubleObjectArray=" + Arrays.toString(doubleObjectArray) +
                    ", booleanObjectArray=" + Arrays.toString(booleanObjectArray) +
                    ", charObjectArray=" + Arrays.toString(charObjectArray) +
                    ", bytePrimitiveArray=" + Arrays.toString(bytePrimitiveArray) +
                    ", shortPrimitiveArray=" + Arrays.toString(shortPrimitiveArray) +
                    ", intPrimitiveArray=" + Arrays.toString(intPrimitiveArray) +
                    ", longPrimitiveArray=" + Arrays.toString(longPrimitiveArray) +
                    ", floatPrimitiveArray=" + Arrays.toString(floatPrimitiveArray) +
                    ", doublePrimitiveArray=" + Arrays.toString(doublePrimitiveArray) +
                    ", booleanPrimitiveArray=" + Arrays.toString(booleanPrimitiveArray) +
                    ", charPrimitiveArray=" + Arrays.toString(charPrimitiveArray) +
                    '}';
        }
    }

    private static class TestData2 {

        private String string;
    }

    private static class TestKey {

        private String string;

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof TestKey)) {
                return false;
            }
            final TestKey testKey = (TestKey) o;
            return Objects.equals(string, testKey.string);
        }

        @Override
        public int hashCode() {

            return Objects.hash(string);
        }
    }
}


