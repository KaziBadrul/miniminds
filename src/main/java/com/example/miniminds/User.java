package com.example.miniminds;

public class User {
    private String name;
    private int age;
    private String email;

    private int math;
    private int spelling;
    private int memory;
    private int iq;
    private int numbers;
    private int timed;

    // ----- Getters -----
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }
    public int getMath() { return math; }
    public int getSpelling() { return spelling; }
    public int getMemory() { return memory; }
    public int getIq() { return iq; }
    public int getNumbers() { return numbers; }
    public int getTimed() { return timed; }

    // ----- Setters -----
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setEmail(String email) { this.email = email; }
    public void setMath(int math) { this.math = math; }
    public void setSpelling(int spelling) { this.spelling = spelling; }
    public void setMemory(int memory) { this.memory = memory; }
    public void setIq(int iq) { this.iq = iq; }
    public void setNumbers(int numbers) { this.numbers = numbers; }
    public void setTimed(int timed) { this.timed = timed; }

    // Optional: compute average level based on scores
    public double getAverageLevel() {
        int total = math + spelling + memory + iq + numbers + timed;
        int count = 6; // number of games
        return total / (double) count / 20; // assuming 20 points per level
    }
}
