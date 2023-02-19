package com.skcraft.launcher.auth;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.skcraft.launcher.auth.microsoft.MicrosoftWebAuthorizer;
import com.skcraft.launcher.auth.microsoft.MinecraftServicesAuthorizer;
import com.skcraft.launcher.auth.microsoft.OauthResult;
import com.skcraft.launcher.auth.microsoft.XboxTokenAuthorizer;
import com.skcraft.launcher.auth.microsoft.model.McAuthResponse;
import com.skcraft.launcher.auth.microsoft.model.McProfileResponse;
import com.skcraft.launcher.auth.microsoft.model.TokenResponse;
import com.skcraft.launcher.auth.microsoft.model.XboxAuthorization;
import com.skcraft.launcher.auth.skin.MinecraftSkinService;
import com.skcraft.launcher.util.HttpRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

import static com.skcraft.launcher.util.HttpRequest.url;

@RequiredArgsConstructor
public class OfflineLoginService implements LoginService {


	public Session login(String id, String password) {

		return new OfflineSession(id);
	}

	@Override
	public Session restore(SavedSession savedSession) {

		return new OfflineSession(savedSession.getUsername());
	}

}
