package com.korobko;

import com.korobko.factories.ColoredShapeFactory;
import com.korobko.factories.ShapeFactory;
import com.korobko.shapes.Line;
import com.korobko.shapes.Point;
import com.korobko.shapes.Shape;
import com.korobko.shapes.Triangle;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;

import static com.korobko.Constants.COLOR;

/**
 * 1. создать последовательность из фигур  используя фабрики цветных и нецветных фигур
 * 2. получить масив цветных и не цветных и сколько каждых фигур в нем присутсвует
 * 3. групируем в масивах фигуры
 * 4. тестируем методы фигур
 * <p>
 * Update from Lesson11:
 * 1) для точек линий и треугольник написать сериализацию и десериализацию
 *    в файл
 * 2) проверка на корректность
 * 3) создать массив фигур записать в файл и прочитать из файла на екран
 *
 * @author Vova Korobko
 */

public class TaskMain {

    public static void main(String[] args) {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(2, 1);
        Point p3 = new Point(2, 3);

        Line line1 = new Line(p1, p2);
        Line line2 = new Line(p2, p3);
        Line line3 = new Line(p1, p3);

        Triangle triangle = new Triangle(p1, p2, p3);
        ShapeSerializator serializator = new ShapeSerializator(Constants.DATAFILE);
        List<Shape> shapes = new ArrayList<>();
        shapes.add(p1);
        shapes.add(p2);
        shapes.add(p3);
        shapes.add(line1);
        shapes.add(line2);
        shapes.add(line3);
        shapes.add(triangle);
        List<Shape> deserializedShapes = null;
        try {
            if (serializator.serialize(shapes)) {
             deserializedShapes = serializator.deserialize();
            }

        } catch (InvalidObjectException e) {
            e.printStackTrace();
        }
        // Positive scenario
        if (deserializedShapes.containsAll(shapes)) {
            System.out.println("Deserialization was correct");
        } else {
            System.out.println("Deserialization was incorrect");
        }

        // Negative scenario
        List<Shape> emptyList = new ArrayList<>();
        if (emptyList.containsAll(shapes)) {
            System.out.println("Deserialization was correct");
        } else {
            System.out.println("Deserialization was incorrect");
        }

        // Negative scenario
        List<Shape> oneElementList = new ArrayList<>();
        oneElementList.add(p1);
        if (oneElementList.containsAll(shapes)) {
            System.out.println("Deserialization was correct");
        } else {
            System.out.println("Deserialization was incorrect");
        }


    }

    /*
     * Generating colored and simple shapes
     */
    public static List<Shape> generateShapes() {
        List<Shape> shapes = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            int key = (int) (Math.random() * 2);
            switch (key) {
                case 0:
                    shapes.add(ColoredShapeFactory.createShape());
                    break;
                case 1:
                    shapes.add(ShapeFactory.createShape());
                    break;
                default:
                    break;
            }
        }
        return shapes;
    }

    /*
     * Counts colored shapes
     */
    public static int countColoredShapes(List<Shape> shapes) {
        return (int) shapes.stream().filter(shape -> shape.containsProperty(COLOR)).count();
    }

    /*
     * Counts simple shapes
     */
    public static int countSimpleShapes(List<Shape> shapes) {
        return (int) shapes.stream().filter(shape -> !shape.containsProperty(COLOR)).count();
    }

    /*
     * Sorts shapes: first simple -> colored, then lexicographically
     */
    public static List<Shape> sortShapes(List<Shape> shapes) {
        shapes.sort(new ShapeByColorComparator()
                .thenComparing(new ShapeLexicographicComparator()));
        return shapes;
    }
}
