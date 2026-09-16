package coding.cat.voidmod.client;

import net.fabricmc.api.ClientModInitializer;

public class VoidsReturnClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		VoidShaderEffect.register();
	}
}