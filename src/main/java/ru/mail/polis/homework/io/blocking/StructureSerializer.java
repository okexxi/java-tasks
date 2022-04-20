package ru.mail.polis.homework.io.blocking;


import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Нужно реализовать методы этого класса и написать сравнительное тестирование 2-ух способов записи и чтения.
 * Для тестирования надо создать список из 10 разных структур (заполнять объекты можно рандомом,
 * с помощью класса Random или руками прописать разные значения переменных).
 * Потом получившийся список записать в один и тот же файл 10 раз (100 раз и более, если у вас это происходит очень быстро).
 * Список и объекты внутри лучше копировать, чтобы у вас не было ссылок на одни и те же объекты.
 * <p>
 * Далее этот список надо прочитать из файла.
 * <p>
 * Результатом теста должно быть следующее: размер файла, время записи и время чтения.
 * Время считать через System.currentTimeMillis().
 * В итоговом пулРеквесте должна быть информация об этих значениях для каждого теста. (всего 2 теста,
 * за правильную генерацию данных 1 тугрик, 1 тугрик за каждый тест и 1 тугрик за правильное объяснение результатов)
 * Для тестов создайте классы в соответствующем пакете в папке тестов. Используйте другие тесты как примеры.
 * <p>
 * В конце теста по чтению данных, не забывайте удалять файлы
 */
public class StructureSerializer {

    /**
     * 1 тугрик
     * Реализовать простую сериализацию, с помощью специального потока для сериализации объектов
     *
     * @param structures Список структур для сериализации
     * @param fileName   файл в который "пишем" структуры
     */
    public void defaultSerialize(List<Structure> structures, String fileName) {
        try {
            FileOutputStream outputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            for (Structure structure : structures) {
                objectOutputStream.writeObject(structure);
            }
            objectOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 1 тугрик
     * Реализовать простую дисериализацию, с помощью специального потока для дисериализации объектов
     *
     * @param fileName файл из которого "читаем" животных
     * @return список структур
     */
    public List<Structure> defaultDeserialize(String fileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            List<Structure> structures = new ArrayList<>();

            try {
                while (objectInputStream.available() == 0) {
                    structures.add((Structure) objectInputStream.readObject());
                }
            } catch (EOFException ignore) {
            }
            objectInputStream.close();
            return structures;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }


    /**
     * 1 тугрик
     * Реализовать сериализацию, с помощью StructureOutputStream
     *
     * @param structures Список структур для сериализации
     * @param fileName   файл в который "пишем" структуры
     */
    public void serialize(List<Structure> structures, String fileName) {
        try {
            File file = new File(fileName);
            StructureOutputStream structureOutputStream = new StructureOutputStream(file);
            structureOutputStream.write(structures.toArray(new Structure[0]));
            structureOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 1 тугрик
     * Реализовать дисериализацию, с помощью StructureInputStream
     *
     * @param fileName файл из которого "читаем" животных
     * @return список структур
     */
    public List<Structure> deserialize(String fileName) {
        try {
            File file = new File(fileName);
            StructureInputStream structureInputStream = new StructureInputStream(file);
            List<Structure> structures = Arrays.asList(structureInputStream.readStructures());
            structureInputStream.close();
            return structures;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}