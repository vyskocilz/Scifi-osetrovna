package main;

import data.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServerData implements Serializable {

    private List<CrewMember> crew;
    private List<MedicTool> tools;
    private List<DiagnosticTool> diagnosticTools;
    private List<Sickness> sicknesses;
    private List<Patient> patients;

    public ServerData() {
        crew = new ArrayList<CrewMember>();
        tools = new ArrayList<MedicTool>();
        diagnosticTools = new ArrayList<DiagnosticTool>();
        sicknesses = new ArrayList<Sickness>();
        patients = new ArrayList<Patient>();
    }

    public List<CrewMember> getCrew() {
        return crew;
    }

    public List<MedicTool> getTools() {
        return tools;
    }

    public List<Sickness> getSicknesses() {
        return sicknesses;
    }

    public List<DiagnosticTool> getDiagnosticTools() {
        return diagnosticTools;
    }

    public void setCrew(List<CrewMember> crew) {
        this.crew = crew;
    }

    public void setTools(List<MedicTool> tools) {
        this.tools = tools;
    }

    public void setDiagnosticTools(List<DiagnosticTool> diagnosticTools) {
        this.diagnosticTools = diagnosticTools;
    }

    public void setSicknesses(List<Sickness> sicknesses) {
        this.sicknesses = sicknesses;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }
}
