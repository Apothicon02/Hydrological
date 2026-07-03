package net.commoble.databuddy.datagen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.mojang.math.Quadrant;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.ConditionBuilder;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.client.renderer.block.model.multipart.CombinedCondition;
import net.minecraft.client.renderer.block.model.multipart.Condition;
import net.minecraft.client.renderer.block.model.multipart.KeyValueCondition;
import net.minecraft.client.renderer.block.model.multipart.Selector;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class BlockStateBuilder
{
	private BlockStateBuilder() {}
	
	// ========== BlockModelGenerators API ==========

	public static Variant variant(Identifier model)
	{
		return new Variant(model);
	}
	
	public static Variant variant(Identifier model, Quadrant xRot, Quadrant yRot, boolean uvLock)
	{
		Variant v = new Variant(model);
		if (xRot != Quadrant.R0)
			v = v.with(VariantMutator.X_ROT.withValue(xRot));
		if (yRot != Quadrant.R0)
			v = v.with(VariantMutator.Y_ROT.withValue(yRot));
		if (uvLock)
			v = v.with(VariantMutator.UV_LOCK.withValue(true));
		return v;
	}
	
	@SafeVarargs
	public static MultiVariant multiVariant(Weighted<Variant>... weightedVariants)
	{
		return new MultiVariant(WeightedList.of(weightedVariants));
	}
	
	public static Weighted<Variant> weighted(Variant variant, int weight)
	{
		return new Weighted<>(variant, weight);
	}
	
	public static void singleVariant(BlockModelGenerators gen, Block block, Identifier model)
	{
		gen.blockStateOutput.accept(
			MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(model)));
	}
	
	public static void singleVariant(BlockModelGenerators gen, Block block, Variant variant)
	{
		gen.blockStateOutput.accept(
			MultiVariantGenerator.dispatch(block, BlockModelGenerators.variant(variant)));
	}
	
	public static MultiVariantGenerator dispatch(Block block, MultiVariant variant)
	{
		return MultiVariantGenerator.dispatch(block, variant);
	}
	
	public static MultiPartGenerator multiPart(Block block)
	{
		return MultiPartGenerator.multiPart(block);
	}
	
	public static void accept(BlockModelGenerators gen, MultiVariantGenerator generator)
	{
		gen.blockStateOutput.accept(generator);
	}
	
	public static void accept(BlockModelGenerators gen, MultiPartGenerator generator)
	{
		gen.blockStateOutput.accept(generator);
	}
	
	public static <T extends Comparable<T>> PropertyDispatch.C1<VariantMutator, T> modify(Property<T> property)
	{
		return PropertyDispatch.modify(property);
	}
	
	public static ConditionBuilder condition()
	{
		return BlockModelGenerators.condition();
	}
	
	public static <T extends Comparable<T>> PropertyDispatch.C1<MultiVariant, T> initial(Property<T> property)
	{
		return PropertyDispatch.initial(property);
	}

	// ========== Codec-based API (JsonDataProvider / BlockModelDefinition) ==========

	public static void addDataProvider(GatherDataEvent event, Map<Identifier, BlockModelDefinition> blockStates)
	{
		JsonDataProvider.addProvider(event, PackOutput.Target.RESOURCE_PACK, "blockstates", BlockModelDefinition.CODEC, blockStates);
	}

	public static BlockModelDefinition singleVariant(BlockStateModel.Unbaked model)
	{
		return new BlockModelDefinition(
			Optional.of(new BlockModelDefinition.SimpleModelSelectors(Map.of("", model))),
			Optional.empty());
	}

	public static BlockModelDefinition variants(Consumer<Variants> variantsBuilder)
	{
		Variants v = Variants.builder();
		variantsBuilder.accept(v);
		return v.build();
	}

	public static BlockModelDefinition variants(Variants variants)
	{
		return variants.build();
	}

	public static BlockModelDefinition multipart(Consumer<Multipart> multipartBuilder)
	{
		Multipart m = Multipart.builder();
		multipartBuilder.accept(m);
		return m.build();
	}

	public static BlockModelDefinition multipart(Multipart multipart)
	{
		return multipart.build();
	}

	// ========== Model factories ==========

	public static BlockStateModel.Unbaked model(Identifier modelId)
	{
		return new SingleVariant.Unbaked(new Variant(modelId));
	}

	public static BlockStateModel.Unbaked model(Identifier modelId, Quadrant xRot, Quadrant yRot, boolean uvLock)
	{
		Variant v = new Variant(modelId);
		if (xRot != Quadrant.R0)
			v = v.with(VariantMutator.X_ROT.withValue(xRot));
		if (yRot != Quadrant.R0)
			v = v.with(VariantMutator.Y_ROT.withValue(yRot));
		if (uvLock)
			v = v.with(VariantMutator.UV_LOCK.withValue(true));
		return new SingleVariant.Unbaked(v);
	}

	public static BlockStateModel.Unbaked model(Identifier modelId, int xDeg, int yDeg, boolean uvLock)
	{
		return model(modelId, fromDegrees(xDeg), fromDegrees(yDeg), uvLock);
	}

	// ========== Variants builder ==========

	public static record Variants(Map<String, BlockStateModel.Unbaked> variants)
	{
		public static Variants builder()
		{
			return new Variants(new LinkedHashMap<>());
		}

		public <T extends Comparable<T>> Variants addVariant(Property<T> property, T value, BlockStateModel.Unbaked model)
		{
			this.variants.put(property.getName() + "=" + property.getName(value), model);
			return this;
		}

		public Variants addMultiPropertyVariant(Consumer<PropertyValueList> listBuilder, BlockStateModel.Unbaked model)
		{
			PropertyValueList list = PropertyValueList.builder();
			listBuilder.accept(list);
			this.variants.put(list.serialized(), model);
			return this;
		}

		public BlockModelDefinition build()
		{
			return new BlockModelDefinition(
				Optional.of(new BlockModelDefinition.SimpleModelSelectors(this.variants)),
				Optional.empty());
		}
	}

	public static record PropertyValueList(List<PropertyValue> propertyValues)
	{
		public static PropertyValueList builder()
		{
			return new PropertyValueList(new ArrayList<>());
		}

		public <T extends Comparable<T>> PropertyValueList addPropertyValue(Property<T> property, T value)
		{
			this.propertyValues.add(new PropertyValue(property, value));
			return this;
		}

		public String serialized()
		{
			return this.propertyValues.stream().map(PropertyValue::serialized).collect(Collectors.joining(","));
		}
	}

	public static record PropertyValue(String propertyName, String valueName)
	{
		public <T extends Comparable<T>> PropertyValue(Property<T> property, T value)
		{
			this(property.getName(), property.getName(value));
		}

		public String serialized()
		{
			return this.propertyName + "=" + this.valueName;
		}
	}

	// ========== Multipart builder ==========

	public static record Multipart(List<WhenApply> cases)
	{
		public static Multipart builder()
		{
			return new Multipart(new ArrayList<>());
		}

		public Multipart apply(BlockStateModel.Unbaked model)
		{
			this.cases.add(new WhenApply(Optional.empty(), model));
			return this;
		}

		@SafeVarargs
		public final <T extends Comparable<T>> Multipart applyWhen(BlockStateModel.Unbaked model, Property<T> property, T value, T... additionalValues)
		{
			return applyWhenAll(model, when -> {
				when.addCondition(property, value, additionalValues);
			});
		}

		public Multipart applyWhenAll(BlockStateModel.Unbaked model, Consumer<When> whenBuilder)
		{
			When when = When.builder();
			whenBuilder.accept(when);
			this.cases.add(new WhenApply(Optional.of(when), model));
			return this;
		}

		public Multipart applyWhenAny(BlockStateModel.Unbaked model, Consumer<OrWhen> orWhenBuilder)
		{
			OrWhen orWhen = OrWhen.builder();
			orWhenBuilder.accept(orWhen);
			this.cases.add(new WhenApply(Optional.of(orWhen), model));
			return this;
		}

		public BlockModelDefinition build()
		{
			List<Selector> selectors = this.cases.stream()
				.map(wa -> new Selector(
					wa.when.map(w -> w.toCondition()),
					wa.apply))
				.toList();
			return new BlockModelDefinition(
				Optional.empty(),
				Optional.of(new BlockModelDefinition.MultiPartDefinition(selectors)));
		}

		public static record WhenApply(Optional<WhenBase> when, BlockStateModel.Unbaked apply) {}
	}

	public sealed interface WhenBase permits When, OrWhen
	{
		Condition toCondition();
	}

	public static record When(Map<String, String> conditions) implements WhenBase
	{
		public static When builder()
		{
			return new When(new LinkedHashMap<>());
		}

		@SafeVarargs
		public final <T extends Comparable<T>> When addCondition(Property<T> property, T value, T... additionalValues)
		{
			StringBuilder combinedValues = new StringBuilder(property.getName(value));
			for (T v : additionalValues)
			{
				combinedValues.append("|").append(property.getName(v));
			}
			this.conditions.put(property.getName(), combinedValues.toString());
			return this;
		}

		@Override
		public Condition toCondition()
		{
			Map<String, KeyValueCondition.Terms> tests = new HashMap<>();
			this.conditions.forEach((key, value) ->
				KeyValueCondition.Terms.parse(value)
					.resultOrPartial(e -> { throw new RuntimeException("Failed to parse condition: " + e); })
					.ifPresent(result -> tests.put(key, result)));
			return new KeyValueCondition(tests);
		}
	}

	public static record OrWhen(List<WhenBase> cases) implements WhenBase
	{
		public static OrWhen builder()
		{
			return new OrWhen(new ArrayList<>());
		}

		public OrWhen addCase(WhenBase case_)
		{
			this.cases.add(case_);
			return this;
		}

		@Override
		public Condition toCondition()
		{
			List<Condition> conditions = this.cases.stream()
				.map(WhenBase::toCondition)
				.toList();
			return new CombinedCondition(
				CombinedCondition.Operation.OR,
				conditions);
		}
	}

	// ========== Internal helpers ==========

	private static Quadrant fromDegrees(int degrees)
	{
		return switch (Mth.positiveModulo(degrees, 360)) {
			case 0 -> Quadrant.R0;
			case 90 -> Quadrant.R90;
			case 180 -> Quadrant.R180;
			case 270 -> Quadrant.R270;
			default -> throw new IllegalArgumentException("Invalid rotation: " + degrees);
		};
	}
}
