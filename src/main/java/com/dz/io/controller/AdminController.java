package com.dz.io.controller;

import com.dz.io.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Api(tags = "Admin operations api")
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(final AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/delete-db")
    @ApiOperation(value = "Deletes database content for a fresh test scenarios")
    public ResponseEntity<?> deleteDatabase(){
        adminService.deleteDatabaseContents();
        return ResponseEntity.ok().build();
    }
}
