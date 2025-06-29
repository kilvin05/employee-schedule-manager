import java.util.*;

class Employee {
    String name;
    Map<String, String> preferences;
    Map<String, String> schedule;
    int daysWorked = 0;

    public Employee(String name, Map<String, String> preferences) {
        this.name = name;
        this.preferences = preferences;
        this.schedule = new HashMap<>();
        for (String day : Scheduler.DAYS) {
            schedule.put(day, null);
        }
    }

    boolean canWork(String day) {
        return schedule.get(day) == null && daysWorked < 5;
    }

    void assignShift(String day, String shift) {
        schedule.put(day, shift);
        daysWorked++;
    }
}

public class Scheduler {
    static final String[] DAYS = {
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };
    static final String[] SHIFTS = {"morning", "afternoon", "evening"};

    Map<String, Map<String, List<String>>> schedule = new HashMap<>();
    List<Employee> employees = new ArrayList<>();
    Random rand = new Random();

    public Scheduler(List<Employee> employees) {
        this.employees = employees;
        for (String day : DAYS) {
            Map<String, List<String>> shiftMap = new HashMap<>();
            for (String shift : SHIFTS) {
                shiftMap.put(shift, new ArrayList<>());
            }
            schedule.put(day, shiftMap);
        }
    }

    void assignShifts() {
        for (String day : DAYS) {
            for (Employee e : employees) {
                if (e.canWork(day)) {
                    String preferredShift = e.preferences.get(day);
                    if (schedule.get(day).get(preferredShift).size() < 2) {
                        schedule.get(day).get(preferredShift).add(e.name);
                        e.assignShift(day, preferredShift);
                    }
                }
            }
        }

        // Fill missing shifts
        for (String day : DAYS) {
            for (String shift : SHIFTS) {
                while (schedule.get(day).get(shift).size() < 2) {
                    List<Employee> available = new ArrayList<>();
                    for (Employee e : employees) {
                        if (e.canWork(day) && !schedule.get(day).get(shift).contains(e.name)) {
                            available.add(e);
                        }
                    }
                    if (available.isEmpty()) break;
                    Employee chosen = available.get(rand.nextInt(available.size()));
                    schedule.get(day).get(shift).add(chosen.name);
                    chosen.assignShift(day, shift);
                }
            }
        }
    }

    void printSchedule() {
        System.out.println("Final Weekly Schedule:\n");
        for (String day : DAYS) {
            System.out.println(day + ":");
            for (String shift : SHIFTS) {
                System.out.println("  " + capitalize(shift) + ": " +
                    String.join(", ", schedule.get(day).get(shift)));
            }
            System.out.println();
        }
    }

    String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static void main(String[] args) {
        List<Employee> emps = new ArrayList<>();
        for (String name : new String[] {"Alice", "Bob", "Charlie", "David", "Eva", "Frank"}) {
            Map<String, String> prefs = new HashMap<>();
            for (String day : DAYS) {
                prefs.put(day, SHIFTS[new Random().nextInt(SHIFTS.length)]);
            }
            emps.add(new Employee(name, prefs));
        }
        Scheduler scheduler = new Scheduler(emps);
        scheduler.assignShifts();
        scheduler.printSchedule();
    }
}