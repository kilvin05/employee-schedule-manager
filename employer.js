const DAYS = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
const SHIFTS = ["morning", "afternoon", "evening"];

class Employee {
  constructor(name, preferences) {
    this.name = name;
    this.preferences = preferences; // { day: preferredShift }
    this.daysWorked = 0;
    this.schedule = {};
    for (const day of DAYS) {
      this.schedule[day] = null;
    }
  }

  canWork(day) {
    return this.schedule[day] === null && this.daysWorked < 5;
  }

  assignShift(day, shift) {
    this.schedule[day] = shift;
    this.daysWorked++;
  }
}

// Generate random preferences for demo
function randomShift() {
  return SHIFTS[Math.floor(Math.random() * SHIFTS.length)];
}

// Create employees
const employees = [
  new Employee("Alice", Object.fromEntries(DAYS.map(d => [d, randomShift()]))),
  new Employee("Bob", Object.fromEntries(DAYS.map(d => [d, randomShift()]))),
  new Employee("Charlie", Object.fromEntries(DAYS.map(d => [d, randomShift()]))),
  new Employee("David", Object.fromEntries(DAYS.map(d => [d, randomShift()]))),
  new Employee("Eva", Object.fromEntries(DAYS.map(d => [d, randomShift()]))),
  new Employee("Frank", Object.fromEntries(DAYS.map(d => [d, randomShift()]))),
];

// Initialize schedule structure
const dayShiftSchedule = {};
for (const day of DAYS) {
  dayShiftSchedule[day] = {};
  for (const shift of SHIFTS) {
    dayShiftSchedule[day][shift] = [];
  }
}

// Assign employees based on preferences
for (const day of DAYS) {
  for (const employee of employees) {
    if (employee.canWork(day)) {
      const preferredShift = employee.preferences[day];
      if (dayShiftSchedule[day][preferredShift].length < 2) {
        dayShiftSchedule[day][preferredShift].push(employee.name);
        employee.assignShift(day, preferredShift);
      }
    }
  }
}

// Fill missing shifts with random available employees
for (const day of DAYS) {
  for (const shift of SHIFTS) {
    while (dayShiftSchedule[day][shift].length < 2) {
      const available = employees.filter(e => e.canWork(day) && !dayShiftSchedule[day][shift].includes(e.name));
      if (available.length === 0) break;
      const chosen = available[Math.floor(Math.random() * available.length)];
      dayShiftSchedule[day][shift].push(chosen.name);
      chosen.assignShift(day, shift);
    }
  }
}

// Output the schedule
console.log("Final Weekly Schedule:\n");
for (const day of DAYS) {
  console.log(day + ":");
  for (const shift of SHIFTS) {
    console.log(`  ${shift.charAt(0).toUpperCase() + shift.slice(1)}: ${dayShiftSchedule[day][shift].join(", ")}`);
  }
  console.log("");
}
