package com.example.AutoSched.GroupObj;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupObjService {
    @Autowired
    private final GroupObjRepository groupRepo;

    public GroupObjService(GroupObjRepository groupObjRepository) {
        this.groupRepo = groupObjRepository;
    }

    public void save(GroupObj group) {
        groupRepo.save(group);
    }

    public void delete(GroupObj group) {
        groupRepo.delete(group);
    }

    public GroupObj findById(int i) {
        return groupRepo.findById(i);
    }

    public GroupObj findByGroupname(String s) {
        return groupRepo.findByGroupname(s);
    }

    public List<GroupObj> findByStartOfName(String s) {
        return groupRepo.findByStartOfName(s);
    }

    public List<GroupObj> findAll() {
        return groupRepo.findAll();
    }

}
