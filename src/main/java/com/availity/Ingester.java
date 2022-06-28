package com.availity;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class Ingester {

    public static void main(String[] args) {
        PriorityQueue<EnrolleeDTO> pq = new PriorityQueue<>((a, b) -> {

            int sort = a.getLastName().compareToIgnoreCase(b.getLastName());
            if (sort != 0) return sort;

            return a.getFirstName().compareToIgnoreCase(b.getFirstName());
        });

        InputStream is = Ingester.class.getClassLoader().getResourceAsStream("data.csv");

        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                var record = line.split(",");

                if (isValidRecord(record)) {
                    var name = record[1].split(" ");
                    pq.add(new EnrolleeDTO(record[0].trim(), name[0], name[name.length -1], Integer.parseInt(record[2]), record[3]));
                }
                else {
                    System.out.println("Invalid data format: " + line);
                    Path path = Paths.get("./results/errors.csv");
                    appendRecordToFile(path, line + "\r\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        HashMap<String, EnrolleeDTO> userIdEnrolleeDTOMap = new HashMap<>();

        var pqIterator = pq.iterator();
        while(pqIterator.hasNext()) {
            var record = pqIterator.next();

            if (userIdEnrolleeDTOMap.containsKey(record.getUserId())) {
                var currentUser = userIdEnrolleeDTOMap.get(record.getUserId());

                if (currentUser.getVersion() < record.getVersion()) {
                    userIdEnrolleeDTOMap.remove(currentUser);
                    userIdEnrolleeDTOMap.put(record.getUserId(), record);
                }
            }
            else {
                userIdEnrolleeDTOMap.put(record.getUserId(), record);
            }
            System.out.println(record.getUserId() + ": " + record.getFirstName() + " " + record.getLastName() + ", " + record.getVersion() + ", " + record.getInsuranceName());
        }

        for(Map.Entry<String, EnrolleeDTO> e : userIdEnrolleeDTOMap.entrySet()) {
            EnrolleeDTO value = e.getValue();

            var insuranceNameTrimmed = value.getInsuranceName().replaceAll("\\s+", "_");

            Path path = Paths.get(String.format("./results/%s.csv", insuranceNameTrimmed));

            appendRecordToFile(path, value.toString() + "\r\n");
        }
    }

    private static void appendRecordToFile(Path path, String record) {
        var dir = path.getParent().toFile();
        if (!dir.exists()) {
            dir.mkdir();
        }

        byte[] data = record.getBytes(StandardCharsets.UTF_8);

        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(path, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException x) {
            System.err.println(x);
        }
    }

    public static boolean isValidRecord(String[] record) {
        return record.length == 4 &&
                !Utils.isEmpty(record[0]) &&
                !Utils.isEmpty(record[1]) &&
                record[1].split(" ").length > 1 &&
                Utils.isInteger(record[2]) &&
                !Utils.isEmpty(record[3]);
    }


}
