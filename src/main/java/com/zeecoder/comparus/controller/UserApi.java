package com.zeecoder.comparus.controller;

import com.zeecoder.comparus.domain.Users;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@OpenAPIDefinition(info=@Info(title="User API"))
@Tag(name = "user", description = "the user API")
public interface UserApi {

    @Operation(summary = "Get all users", tags = { "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Users.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Users not found", content = @Content) })
    ResponseEntity<List<Users>> retriveAllUsers();
}

