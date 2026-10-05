package com.atlas.bank.atlas_bank.account.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atlas.bank.atlas_bank.account.dto.AccountMapper;
import com.atlas.bank.atlas_bank.account.dto.AccountResponse;
import com.atlas.bank.atlas_bank.account.dto.CreateAccountRequest;
import com.atlas.bank.atlas_bank.account.model.Account;
import com.atlas.bank.atlas_bank.account.service.IAccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final IAccountService iAccountService;
    private final AccountMapper accountMapper;

    @PostMapping
    // @valid antes del requestbody para que spring valide el request body y lance
    // una excepcion si no es valido.
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {
        Account account = accountMapper.toEntity(request);
        Account saved = iAccountService.create(account);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountMapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll() {
        var accounts = iAccountService.findAll();
        var response = accounts.stream().map(accountMapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(accountMapper.toResponse(iAccountService.findById(id)));
    }

}
