package com.udacity.jdnd.course3.critter.schedule;
import com.udacity.jdnd.course3.critter.exception.UserNotFoundException;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    PetService petService;

    @Autowired
    ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        //create a new schedule

        Schedule schedule = scheduleService.createSchedule(convertScheduleDTOToSchedule(scheduleDTO));
        return convertScheduleToScheduleDTO(schedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        // Get a list of all Schedules

        List<Schedule> scheduleList = scheduleService.getAllSchedules();
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            scheduleDTOList.add(convertScheduleToScheduleDTO(schedule));
        }
        return scheduleDTOList;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        //Get Schedule for pet

        List<Schedule> scheduleList = scheduleService.getScheduleForPet(petId);
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            scheduleDTOList.add(convertScheduleToScheduleDTO(schedule));
        }
        return scheduleDTOList;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        //Get Schedule for Employees
        List<Schedule> scheduleList = scheduleService.getScheduleForEmployee(employeeId);
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();

        for (Schedule schedule : scheduleList) {
            scheduleDTOList.add(convertScheduleToScheduleDTO(schedule));
        }
        return scheduleDTOList;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        //Get Schedules For Customer

        List<Schedule> scheduleList = scheduleService.getScheduleForCustomer(customerId);
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            scheduleDTOList.add(convertScheduleToScheduleDTO(schedule));
        }
        return scheduleDTOList;
    }

    private Schedule convertScheduleDTOToSchedule(ScheduleDTO scheduleDTO) {
        //Convert ScheduleDto To Schedule

        Schedule schedule = new Schedule(); // creating a new Schedule object
        BeanUtils.copyProperties(scheduleDTO, schedule); // copy the properties from scheduleDTO to schedule
        schedule.setActivities(scheduleDTO.getActivities());
        HashMap<Long, Employee> map = new HashMap<>(); // Make a new Hashmap where key is Long and value is Employee.
        for (Long employeeId : scheduleDTO.getEmployeeIds()) {
            Optional<Employee> optionalEmployee = Optional.ofNullable(employeeService.getEmployeeById(employeeId));
            if (optionalEmployee.isPresent()) {
                map.put(employeeId, optionalEmployee.get());
            } else {
                throw new UserNotFoundException("Employee not found, please try again.");
            }
        }
        schedule.setEmployees(new ArrayList<Employee>(map.values()));
        HashMap<Long, Pet> petHashMap = new HashMap<>();
        for (Long petId : scheduleDTO.getPetIds()) {
            Optional<Pet> optionalPet = Optional.ofNullable(petService.getPetById(petId));
            if (optionalPet.isPresent()) {
                petHashMap.put(petId, optionalPet.get());
            } else {
                throw new UserNotFoundException("Pet not found, please try again.");
            }
        }
        schedule.setPets(new ArrayList<Pet>(petHashMap.values()));
        return schedule;
    }

    private static ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule) {
        //ConvertScheduleToScheduleDTO

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);

        scheduleDTO.setActivities(schedule.getActivities());

        List<Pet> petList = schedule.getPets();
        List<Long> petId = new ArrayList<>();
        for (Pet pet : petList) {
            petId.add(pet.getId());
        }
        scheduleDTO.setPetIds(petId);
        List<Employee> employeeList = schedule.getEmployees();
        List<Long> employeeId = new ArrayList<>();
        for (Employee employee : employeeList) {
            employeeId.add(employee.getId());
        }
        scheduleDTO.setEmployeeIds(employeeId);
        return scheduleDTO;
    }

}

