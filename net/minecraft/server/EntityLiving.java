package net.minecraft.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public abstract class EntityLiving extends Entity {

    public int maxNoDamageTicks = 20;
    public float T;
    public float U;
    public float V = 0.0F;
    public float W = 0.0F;
    public float X = 0.0F;
    public float Y = 0.0F;
    protected float Z;
    protected float aa;
    protected float ab;
    protected float ac;
    protected boolean ad = true;
    protected String texture = "/mob/char.png";
    protected boolean af = true;
    protected float ag = 0.0F;
    protected String ah = null;
    protected float ai = 1.0F;
    protected int aj = 0;
    protected float ak = 0.0F;
    public float al = 0.1F;
    public float am = 0.02F;
    public float an;
    public float ao;
    protected int health = this.getMaxHealth();
    public int aq;
    protected int ar;
    private int a;
    public int hurtTicks;
    public int at;
    public float au = 0.0F;
    public int deathTicks = 0;
    public int attackTicks = 0;
    public float ax;
    public float ay;
    protected boolean az = false;
    protected int aA;
    public int aB = -1;
    public float aC = (float) (Math.random() * 0.8999999761581421D + 0.10000000149011612D);
    public float aD;
    public float aE;
    public float aF;
    protected EntityHuman killer = null;
    protected int lastDamageByPlayerTime = 0;
    protected EntityLiving lastDamager = null;
    public int aJ = 0;
    public int aK = 0;
    protected HashMap effects = new HashMap();
    private boolean b = true;
    private int c;
    private ControllerLook lookController;
    private ControllerMove moveController;
    private ControllerJump jumpController;
    private Navigation navigation;
    protected PathfinderGoalSelector goalSelector = new PathfinderGoalSelector();
    protected int aN;
    protected double aO;
    protected double aP;
    protected double aQ;
    protected double aR;
    protected double aS;
    float aT = 0.0F;
    protected int lastDamage = 0;
    protected int aV = 0;
    protected float aW;
    protected float aX;
    protected float aY;
    protected boolean aZ = false;
    protected float ba = 0.0F;
    protected float bb = 0.7F;
    private int h = 0;
    private Entity i;
    protected int bc = 0;

    public EntityLiving(World world) {
        super(world);
        this.bf = true;
        this.lookController = new ControllerLook(this);
        this.moveController = new ControllerMove(this, this.bb);
        this.jumpController = new ControllerJump(this);
        this.navigation = new PathfinderNavigation(this, world);
        this.U = (float) (Math.random() + 1.0D) * 0.01F;
        this.setPosition(this.locX, this.locY, this.locZ);
        this.T = (float) Math.random() * 12398.0F;
        this.yaw = (float) (Math.random() * 3.1415927410125732D * 2.0D);
        this.X = this.yaw;
        this.bP = 0.5F;
    }

    public ControllerLook getControllerLook() {
        return this.lookController;
    }

    public ControllerMove getControllerMove() {
        return this.moveController;
    }

    public ControllerJump getControllerJump() {
        return this.jumpController;
    }

    public Navigation ah() {
        return this.navigation;
    }

    public Random ai() {
        return this.random;
    }

    public EntityLiving aj() {
        return this.lastDamager;
    }

    public int ak() {
        return this.aV;
    }

    protected void b() {
        this.datawatcher.a(8, Integer.valueOf(this.c));
    }

    public boolean g(Entity entity) {
        return this.world.a(Vec3D.create(this.locX, this.locY + (double) this.y(), this.locZ), Vec3D.create(entity.locX, entity.locY + (double) entity.y(), entity.locZ)) == null;
    }

    public boolean e_() {
        return !this.dead;
    }

    public boolean f_() {
        return !this.dead;
    }

    public float y() {
        return this.length * 0.85F;
    }

    public int h() {
        return 80;
    }

    public void al() {
        String s = this.c_();

        if (s != null) {
            this.world.makeSound(this, s, this.o(), this.v());
        }
    }

    public void am() {
        this.an = this.ao;
        super.am();
        MethodProfiler.a("mobBaseTick");
        if (this.random.nextInt(1000) < this.a++) {
            this.a = -this.h();
            this.al();
        }

        if (this.isAlive() && this.inBlock() && this.damageEntity(DamageSource.STUCK, 1)) {
            ;
        }

        if (this.isFireproof() || this.world.isStatic) {
            this.extinguish();
        }

        if (this.isAlive() && this.a(Material.WATER) && !this.f() && !this.effects.containsKey(Integer.valueOf(MobEffectList.WATER_BREATHING.id))) {
            this.setAirTicks(this.f(this.getAirTicks()));
            if (this.getAirTicks() == -20) {
                this.setAirTicks(0);

                for (int i = 0; i < 8; ++i) {
                    float f = this.random.nextFloat() - this.random.nextFloat();
                    float f1 = this.random.nextFloat() - this.random.nextFloat();
                    float f2 = this.random.nextFloat() - this.random.nextFloat();

                    this.world.a("bubble", this.locX + (double) f, this.locY + (double) f1, this.locZ + (double) f2, this.motX, this.motY, this.motZ);
                }

                this.damageEntity(DamageSource.DROWN, 2);
            }

            this.extinguish();
        } else {
            this.setAirTicks(300);
        }

        this.ax = this.ay;
        if (this.attackTicks > 0) {
            --this.attackTicks;
        }

        if (this.hurtTicks > 0) {
            --this.hurtTicks;
        }

        if (this.noDamageTicks > 0) {
            --this.noDamageTicks;
        }

        if (this.health <= 0) {
            this.an();
        }

        if (this.lastDamageByPlayerTime > 0) {
            --this.lastDamageByPlayerTime;
        } else {
            this.killer = null;
        }

        this.aB();
        this.ac = this.ab;
        this.W = this.V;
        this.Y = this.X;
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
        MethodProfiler.a();
    }

    protected void an() {
        ++this.deathTicks;
        if (this.deathTicks == 20) {
            int i;

            if (!this.world.isStatic && (this.lastDamageByPlayerTime > 0 || this.alwaysGivesExp()) && !this.isBaby()) {
                i = this.getExpValue(this.killer);

                while (i > 0) {
                    int j = EntityExperienceOrb.getOrbValue(i);

                    i -= j;
                    this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, j));
                }
            }

            this.ay();
            this.die();

            for (i = 0; i < 20; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;

                this.world.a("explode", this.locX + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, this.locY + (double) (this.random.nextFloat() * this.length), this.locZ + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
            }
        }
    }

    protected int f(int i) {
        return i - 1;
    }

    protected int getExpValue(EntityHuman entityhuman) {
        return this.aA;
    }

    protected boolean alwaysGivesExp() {
        return false;
    }

    public void ao() {
        for (int i = 0; i < 20; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            double d3 = 10.0D;

            this.world.a("explode", this.locX + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width - d0 * d3, this.locY + (double) (this.random.nextFloat() * this.length) - d1 * d3, this.locZ + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width - d2 * d3, d0, d1, d2);
        }
    }

    public void N() {
        super.N();
        this.Z = this.aa;
        this.aa = 0.0F;
        this.fallDistance = 0.0F;
    }

    public void y_() {
        super.y_();
        if (this.aJ > 0) {
            if (this.aK <= 0) {
                this.aK = 60;
            }

            --this.aK;
            if (this.aK <= 0) {
                --this.aJ;
            }
        }

        this.d();
        double d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f = MathHelper.sqrt(d0 * d0 + d1 * d1);
        float f1 = this.V;
        float f2 = 0.0F;

        this.Z = this.aa;
        float f3 = 0.0F;

        if (f > 0.05F) {
            f3 = 1.0F;
            f2 = f * 3.0F;
            f1 = (float) Math.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
        }

        if (this.ao > 0.0F) {
            f1 = this.yaw;
        }

        if (!this.onGround) {
            f3 = 0.0F;
        }

        this.aa += (f3 - this.aa) * 0.3F;

        float f4;

        for (f4 = f1 - this.V; f4 < -180.0F; f4 += 360.0F) {
            ;
        }

        while (f4 >= 180.0F) {
            f4 -= 360.0F;
        }

        this.V += f4 * 0.3F;

        float f5;

        for (f5 = this.yaw - this.V; f5 < -180.0F; f5 += 360.0F) {
            ;
        }

        while (f5 >= 180.0F) {
            f5 -= 360.0F;
        }

        boolean flag = f5 < -90.0F || f5 >= 90.0F;

        if (f5 < -75.0F) {
            f5 = -75.0F;
        }

        if (f5 >= 75.0F) {
            f5 = 75.0F;
        }

        this.V = this.yaw - f5;
        if (f5 * f5 > 2500.0F) {
            this.V += f5 * 0.2F;
        }

        if (flag) {
            f2 *= -1.0F;
        }

        while (this.yaw - this.lastYaw < -180.0F) {
            this.lastYaw -= 360.0F;
        }

        while (this.yaw - this.lastYaw >= 180.0F) {
            this.lastYaw += 360.0F;
        }

        while (this.V - this.W < -180.0F) {
            this.W -= 360.0F;
        }

        while (this.V - this.W >= 180.0F) {
            this.W += 360.0F;
        }

        while (this.pitch - this.lastPitch < -180.0F) {
            this.lastPitch -= 360.0F;
        }

        while (this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
        }

        this.ab += f2;
    }

    protected void b(float f, float f1) {
        super.b(f, f1);
    }

    public void heal(int i) {
        if (this.health > 0) {
            this.health += i;
            if (this.health > this.getMaxHealth()) {
                this.health = this.getMaxHealth();
            }

            this.noDamageTicks = this.maxNoDamageTicks / 2;
        }
    }

    public abstract int getMaxHealth();

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int i) {
        this.health = i;
        if (i > this.getMaxHealth()) {
            i = this.getMaxHealth();
        }
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (this.world.isStatic) {
            return false;
        } else {
            this.aV = 0;
            if (this.health <= 0) {
                return false;
            } else if (damagesource.k() && this.hasEffect(MobEffectList.FIRE_RESISTANCE)) {
                return false;
            } else {
                this.aE = 1.5F;
                boolean flag = true;

                if ((float) this.noDamageTicks > (float) this.maxNoDamageTicks / 2.0F) {
                    if (i <= this.lastDamage) {
                        return false;
                    }

                    this.c(damagesource, i - this.lastDamage);
                    this.lastDamage = i;
                    flag = false;
                } else {
                    this.lastDamage = i;
                    this.aq = this.health;
                    this.noDamageTicks = this.maxNoDamageTicks;
                    this.c(damagesource, i);
                    this.hurtTicks = this.at = 10;
                }

                this.au = 0.0F;
                Entity entity = damagesource.getEntity();

                if (entity != null) {
                    if (entity instanceof EntityHuman) {
                        this.lastDamageByPlayerTime = 60;
                        this.killer = (EntityHuman) entity;
                    } else if (entity instanceof EntityWolf) {
                        EntityWolf entitywolf = (EntityWolf) entity;

                        if (entitywolf.isTamed()) {
                            this.lastDamageByPlayerTime = 60;
                            this.killer = null;
                        }
                    }
                }

                if (flag) {
                    this.world.a(this, (byte) 2);
                    this.aM();
                    if (entity != null) {
                        double d0 = entity.locX - this.locX;

                        double d1;

                        for (d1 = entity.locZ - this.locZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                            d0 = (Math.random() - Math.random()) * 0.01D;
                        }

                        this.au = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - this.yaw;
                        this.a(entity, i, d0, d1);
                    } else {
                        this.au = (float) ((int) (Math.random() * 2.0D) * 180);
                    }
                }

                if (this.health <= 0) {
                    if (flag) {
                        this.world.makeSound(this, this.n(), this.o(), this.v());
                    }

                    this.die(damagesource);
                } else if (flag) {
                    this.world.makeSound(this, this.m(), this.o(), this.v());
                }

                return true;
            }
        }
    }

    private float v() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
    }

    public int P() {
        return 0;
    }

    protected void g(int i) {}

    protected int d(DamageSource damagesource, int i) {
        if (!damagesource.ignoresArmor()) {
            int j = 25 - this.P();
            int k = i * j + this.ar;

            this.g(i);
            i = k / 25;
            this.ar = k % 25;
        }

        return i;
    }

    protected int b(DamageSource damagesource, int i) {
        if (this.hasEffect(MobEffectList.RESISTANCE)) {
            int j = (this.getEffect(MobEffectList.RESISTANCE).getAmplifier() + 1) * 5;
            int k = 25 - j;
            int l = i * k + this.ar;

            i = l / 25;
            this.ar = l % 25;
        }

        return i;
    }

    protected void c(DamageSource damagesource, int i) {
        i = this.d(damagesource, i);
        i = this.b(damagesource, i);
        this.health -= i;
    }

    protected float o() {
        return 1.0F;
    }

    protected String c_() {
        return null;
    }

    protected String m() {
        return "damage.hurtflesh";
    }

    protected String n() {
        return "damage.hurtflesh";
    }

    public void a(Entity entity, int i, double d0, double d1) {
        this.ce = true;
        float f = MathHelper.sqrt(d0 * d0 + d1 * d1);
        float f1 = 0.4F;

        this.motX /= 2.0D;
        this.motY /= 2.0D;
        this.motZ /= 2.0D;
        this.motX -= d0 / (double) f * (double) f1;
        this.motY += (double) f1;
        this.motZ -= d1 / (double) f * (double) f1;
        if (this.motY > 0.4000000059604645D) {
            this.motY = 0.4000000059604645D;
        }
    }

    public void die(DamageSource damagesource) {
        Entity entity = damagesource.getEntity();

        if (this.aj >= 0 && entity != null) {
            entity.b(this, this.aj);
        }

        if (entity != null) {
            entity.a(this);
        }

        this.az = true;
        if (!this.world.isStatic) {
            int i = 0;

            if (entity instanceof EntityHuman) {
                i = EnchantmentManager.getBonusMonsterLootEnchantmentLevel(((EntityHuman) entity).inventory);
            }

            if (!this.isBaby()) {
                this.dropDeathLoot(this.lastDamageByPlayerTime > 0, i);
            }
        }

        this.world.a(this, (byte) 3);
    }

    protected void dropDeathLoot(boolean flag, int i) {
        int j = this.getLootId();

        if (j > 0) {
            int k = this.random.nextInt(3);

            if (i > 0) {
                k += this.random.nextInt(i + 1);
            }

            for (int l = 0; l < k; ++l) {
                this.b(j, 1);
            }
        }
    }

    protected int getLootId() {
        return 0;
    }

    protected void b(float f) {
        super.b(f);
        int i = (int) Math.ceil((double) (f - 3.0F));

        if (i > 0) {
            if (i > 4) {
                this.world.makeSound(this, "damage.fallbig", 1.0F, 1.0F);
            } else {
                this.world.makeSound(this, "damage.fallsmall", 1.0F, 1.0F);
            }

            this.damageEntity(DamageSource.FALL, i);
            int j = this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.locY - 0.20000000298023224D - (double) this.height), MathHelper.floor(this.locZ));

            if (j > 0) {
                StepSound stepsound = Block.byId[j].stepSound;

                this.world.makeSound(this, stepsound.getName(), stepsound.getVolume1() * 0.5F, stepsound.getVolume2() * 0.75F);
            }
        }
    }

    public void a(float f, float f1) {
        double d0;

        if (this.aK()) {
            d0 = this.locY;
            this.a(f, f1, 0.02F);
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= 0.800000011920929D;
            this.motY *= 0.800000011920929D;
            this.motZ *= 0.800000011920929D;
            this.motY -= 0.02D;
            if (this.positionChanged && this.d(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ)) {
                this.motY = 0.30000001192092896D;
            }
        } else if (this.aL()) {
            d0 = this.locY;
            this.a(f, f1, 0.02F);
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= 0.5D;
            this.motY *= 0.5D;
            this.motZ *= 0.5D;
            this.motY -= 0.02D;
            if (this.positionChanged && this.d(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ)) {
                this.motY = 0.30000001192092896D;
            }
        } else {
            float f2 = 0.91F;

            if (this.onGround) {
                f2 = 0.54600006F;
                int i = this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.locZ));

                if (i > 0) {
                    f2 = Block.byId[i].frictionFactor * 0.91F;
                }
            }

            float f3 = 0.16277136F / (f2 * f2 * f2);
            float f4 = this.onGround ? this.al * f3 : this.am;

            this.a(f, f1, f4);
            f2 = 0.91F;
            if (this.onGround) {
                f2 = 0.54600006F;
                int j = this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.locZ));

                if (j > 0) {
                    f2 = Block.byId[j].frictionFactor * 0.91F;
                }
            }

            if (this.r()) {
                float f5 = 0.15F;

                if (this.motX < (double) (-f5)) {
                    this.motX = (double) (-f5);
                }

                if (this.motX > (double) f5) {
                    this.motX = (double) f5;
                }

                if (this.motZ < (double) (-f5)) {
                    this.motZ = (double) (-f5);
                }

                if (this.motZ > (double) f5) {
                    this.motZ = (double) f5;
                }

                this.fallDistance = 0.0F;
                if (this.motY < -0.15D) {
                    this.motY = -0.15D;
                }

                if (this.isSneaking() && this.motY < 0.0D) {
                    this.motY = 0.0D;
                }
            }

            this.move(this.motX, this.motY, this.motZ);
            if (this.positionChanged && this.r()) {
                this.motY = 0.2D;
            }

            this.motY -= 0.08D;
            this.motY *= 0.9800000190734863D;
            this.motX *= (double) f2;
            this.motZ *= (double) f2;
        }

        this.aD = this.aE;
        d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f6 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

        if (f6 > 1.0F) {
            f6 = 1.0F;
        }

        this.aE += (f6 - this.aE) * 0.4F;
        this.aF += this.aE;
    }

    public boolean r() {
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.boundingBox.b);
        int k = MathHelper.floor(this.locZ);

        return this.world.getTypeId(i, j, k) == Block.LADDER.id;
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("Health", (short) this.health);
        nbttagcompound.setShort("HurtTime", (short) this.hurtTicks);
        nbttagcompound.setShort("DeathTime", (short) this.deathTicks);
        nbttagcompound.setShort("AttackTime", (short) this.attackTicks);
        if (!this.effects.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = this.effects.values().iterator();

            while (iterator.hasNext()) {
                MobEffect mobeffect = (MobEffect) iterator.next();
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.setByte("Id", (byte) mobeffect.getEffectId());
                nbttagcompound1.setByte("Amplifier", (byte) mobeffect.getAmplifier());
                nbttagcompound1.setInt("Duration", mobeffect.getDuration());
                nbttaglist.add(nbttagcompound1);
            }

            nbttagcompound.set("ActiveEffects", nbttaglist);
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.health = nbttagcompound.getShort("Health");
        if (!nbttagcompound.hasKey("Health")) {
            this.health = this.getMaxHealth();
        }

        this.hurtTicks = nbttagcompound.getShort("HurtTime");
        this.deathTicks = nbttagcompound.getShort("DeathTime");
        this.attackTicks = nbttagcompound.getShort("AttackTime");
        if (nbttagcompound.hasKey("ActiveEffects")) {
            NBTTagList nbttaglist = nbttagcompound.getList("ActiveEffects");

            for (int i = 0; i < nbttaglist.size(); ++i) {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.get(i);
                byte b0 = nbttagcompound1.getByte("Id");
                byte b1 = nbttagcompound1.getByte("Amplifier");
                int j = nbttagcompound1.getInt("Duration");

                this.effects.put(Integer.valueOf(b0), new MobEffect(b0, j, b1));
            }
        }
    }

    public boolean isAlive() {
        return !this.dead && this.health > 0;
    }

    public boolean f() {
        return false;
    }

    public void d(float f) {
        this.aX = f;
    }

    public void e(boolean flag) {
        this.aZ = flag;
    }

    public float ar() {
        return this.bb;
    }

    public void d() {
        if (this.h > 0) {
            --this.h;
        }

        if (this.aN > 0) {
            double d0 = this.locX + (this.aO - this.locX) / (double) this.aN;
            double d1 = this.locY + (this.aP - this.locY) / (double) this.aN;
            double d2 = this.locZ + (this.aQ - this.locZ) / (double) this.aN;

            double d3;

            for (d3 = this.aR - (double) this.yaw; d3 < -180.0D; d3 += 360.0D) {
                ;
            }

            while (d3 >= 180.0D) {
                d3 -= 360.0D;
            }

            this.yaw = (float) ((double) this.yaw + d3 / (double) this.aN);
            this.pitch = (float) ((double) this.pitch + (this.aS - (double) this.pitch) / (double) this.aN);
            --this.aN;
            this.setPosition(d0, d1, d2);
            this.c(this.yaw, this.pitch);
            List list = this.world.a((Entity) this, this.boundingBox.shrink(0.03125D, 0.0D, 0.03125D));

            if (list.size() > 0) {
                double d4 = 0.0D;

                for (int i = 0; i < list.size(); ++i) {
                    AxisAlignedBB axisalignedbb = (AxisAlignedBB) list.get(i);

                    if (axisalignedbb.e > d4) {
                        d4 = axisalignedbb.e;
                    }
                }

                d1 += d4 - this.boundingBox.b;
                this.setPosition(d0, d1, d2);
            }
        }

        MethodProfiler.a("ai");
        if (this.M()) {
            this.aZ = false;
            this.aW = 0.0F;
            this.aX = 0.0F;
            this.aY = 0.0F;
        } else if (this.at()) {
            if (this.as()) {
                this.av();
            } else {
                this.m_();
                this.X = this.yaw;
            }
        }

        MethodProfiler.a();
        boolean flag = this.aK();
        boolean flag1 = this.aL();

        if (this.aZ) {
            if (flag) {
                this.motY += 0.03999999910593033D;
            } else if (flag1) {
                this.motY += 0.03999999910593033D;
            } else if (this.onGround && this.h == 0) {
                this.o_();
                this.h = 10;
            }
        } else {
            this.h = 0;
        }

        this.aW *= 0.98F;
        this.aX *= 0.98F;
        this.aY *= 0.9F;
        float f = this.al;

        this.al *= this.G();
        this.a(this.aW, this.aX);
        this.al = f;
        MethodProfiler.a("push");
        List list1 = this.world.getEntities(this, this.boundingBox.grow(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list1 != null && list1.size() > 0) {
            for (int j = 0; j < list1.size(); ++j) {
                Entity entity = (Entity) list1.get(j);

                if (entity.f_()) {
                    entity.collide(this);
                }
            }
        }

        MethodProfiler.a();
    }

    protected boolean as() {
        return false;
    }

    protected boolean at() {
        return !this.world.isStatic;
    }

    protected boolean M() {
        return this.health <= 0;
    }

    public boolean L() {
        return false;
    }

    protected void o_() {
        this.motY = 0.41999998688697815D;
        if (this.hasEffect(MobEffectList.JUMP)) {
            this.motY += (double) ((float) (this.getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.1F);
        }

        if (this.isSprinting()) {
            float f = this.yaw * 0.017453292F;

            this.motX -= (double) (MathHelper.sin(f) * 0.2F);
            this.motZ += (double) (MathHelper.cos(f) * 0.2F);
        }

        this.ce = true;
    }

    protected boolean d_() {
        return true;
    }

    protected void au() {
        EntityHuman entityhuman = this.world.findNearbyPlayer(this, -1.0D);

        if (entityhuman != null) {
            double d0 = entityhuman.locX - this.locX;
            double d1 = entityhuman.locY - this.locY;
            double d2 = entityhuman.locZ - this.locZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (this.d_() && d3 > 16384.0D) {
                this.die();
            }

            if (this.aV > 600 && this.random.nextInt(800) == 0 && d3 > 1024.0D && this.d_()) {
                this.die();
            } else if (d3 < 1024.0D) {
                this.aV = 0;
            }
        }
    }

    protected void av() {
        ++this.aV;
        this.au();
        if (this.lastDamager != null && !this.lastDamager.isAlive()) {
            this.lastDamager = null;
        }

        this.goalSelector.a();
        this.navigation.a();
        this.moveController.a();
        this.lookController.a();
        this.jumpController.b();
    }

    protected void m_() {
        ++this.aV;
        EntityHuman entityhuman = this.world.findNearbyPlayer(this, -1.0D);

        this.au();
        this.aW = 0.0F;
        this.aX = 0.0F;
        float f = 8.0F;

        if (this.random.nextFloat() < 0.02F) {
            entityhuman = this.world.findNearbyPlayer(this, (double) f);
            if (entityhuman != null) {
                this.i = entityhuman;
                this.bc = 10 + this.random.nextInt(20);
            } else {
                this.aY = (this.random.nextFloat() - 0.5F) * 20.0F;
            }
        }

        if (this.i != null) {
            this.a(this.i, 10.0F, (float) this.x());
            if (this.bc-- <= 0 || this.i.dead || this.i.i(this) > (double) (f * f)) {
                this.i = null;
            }
        } else {
            if (this.random.nextFloat() < 0.05F) {
                this.aY = (this.random.nextFloat() - 0.5F) * 20.0F;
            }

            this.yaw += this.aY;
            this.pitch = this.ba;
        }

        boolean flag = this.aK();
        boolean flag1 = this.aL();

        if (flag || flag1) {
            this.aZ = this.random.nextFloat() < 0.8F;
        }
    }

    public int x() {
        return 40;
    }

    public void a(Entity entity, float f, float f1) {
        double d0 = entity.locX - this.locX;
        double d1 = entity.locZ - this.locZ;
        double d2;

        if (entity instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving) entity;

            d2 = this.locY + (double) this.y() - (entityliving.locY + (double) entityliving.y());
        } else {
            d2 = (entity.boundingBox.b + entity.boundingBox.e) / 2.0D - (this.locY + (double) this.y());
        }

        double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1);
        float f2 = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
        float f3 = (float) (-(Math.atan2(d2, d3) * 180.0D / 3.1415927410125732D));

        this.pitch = -this.b(this.pitch, f3, f1);
        this.yaw = this.b(this.yaw, f2, f);
    }

    public boolean aw() {
        return this.i != null;
    }

    public Entity ax() {
        return this.i;
    }

    private float b(float f, float f1, float f2) {
        float f3;

        for (f3 = f1 - f; f3 < -180.0F; f3 += 360.0F) {
            ;
        }

        while (f3 >= 180.0F) {
            f3 -= 360.0F;
        }

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        return f + f3;
    }

    public void ay() {}

    public boolean canSpawn() {
        return this.world.containsEntity(this.boundingBox) && this.world.a((Entity) this, this.boundingBox).size() == 0 && !this.world.c(this.boundingBox);
    }

    protected void az() {
        this.damageEntity(DamageSource.OUT_OF_WORLD, 4);
    }

    public Vec3D aA() {
        return this.e(1.0F);
    }

    public Vec3D e(float f) {
        float f1;
        float f2;
        float f3;
        float f4;

        if (f == 1.0F) {
            f1 = MathHelper.cos(-this.yaw * 0.017453292F - 3.1415927F);
            f2 = MathHelper.sin(-this.yaw * 0.017453292F - 3.1415927F);
            f3 = -MathHelper.cos(-this.pitch * 0.017453292F);
            f4 = MathHelper.sin(-this.pitch * 0.017453292F);
            return Vec3D.create((double) (f2 * f3), (double) f4, (double) (f1 * f3));
        } else {
            f1 = this.lastPitch + (this.pitch - this.lastPitch) * f;
            f2 = this.lastYaw + (this.yaw - this.lastYaw) * f;
            f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
            f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
            float f5 = -MathHelper.cos(-f1 * 0.017453292F);
            float f6 = MathHelper.sin(-f1 * 0.017453292F);

            return Vec3D.create((double) (f4 * f5), (double) f6, (double) (f3 * f5));
        }
    }

    public int p() {
        return 4;
    }

    public boolean isSleeping() {
        return false;
    }

    protected void aB() {
        Iterator iterator = this.effects.keySet().iterator();

        while (iterator.hasNext()) {
            Integer integer = (Integer) iterator.next();
            MobEffect mobeffect = (MobEffect) this.effects.get(integer);

            if (!mobeffect.tick(this) && !this.world.isStatic) {
                iterator.remove();
                this.d(mobeffect);
            }
        }

        int i;

        if (this.b) {
            if (!this.world.isStatic) {
                if (!this.effects.isEmpty()) {
                    i = PotionBrewer.a(this.effects.values());
                    this.datawatcher.watch(8, Integer.valueOf(i));
                } else {
                    this.datawatcher.watch(8, Integer.valueOf(0));
                }
            }

            this.b = false;
        }

        if (this.random.nextBoolean()) {
            i = this.datawatcher.getInt(8);
            if (i > 0) {
                double d0 = (double) (i >> 16 & 255) / 255.0D;
                double d1 = (double) (i >> 8 & 255) / 255.0D;
                double d2 = (double) (i >> 0 & 255) / 255.0D;

                this.world.a("mobSpell", this.locX + (this.random.nextDouble() - 0.5D) * (double) this.width, this.locY + this.random.nextDouble() * (double) this.length - (double) this.height, this.locZ + (this.random.nextDouble() - 0.5D) * (double) this.width, d0, d1, d2);
            }
        }
    }

    public void aC() {
        Iterator iterator = this.effects.keySet().iterator();

        while (iterator.hasNext()) {
            Integer integer = (Integer) iterator.next();
            MobEffect mobeffect = (MobEffect) this.effects.get(integer);

            if (!this.world.isStatic) {
                iterator.remove();
                this.d(mobeffect);
            }
        }
    }

    public Collection getEffects() {
        return this.effects.values();
    }

    public boolean hasEffect(MobEffectList mobeffectlist) {
        return this.effects.containsKey(Integer.valueOf(mobeffectlist.id));
    }

    public MobEffect getEffect(MobEffectList mobeffectlist) {
        return (MobEffect) this.effects.get(Integer.valueOf(mobeffectlist.id));
    }

    public void addEffect(MobEffect mobeffect) {
        if (this.a(mobeffect)) {
            if (this.effects.containsKey(Integer.valueOf(mobeffect.getEffectId()))) {
                ((MobEffect) this.effects.get(Integer.valueOf(mobeffect.getEffectId()))).a(mobeffect);
                this.c((MobEffect) this.effects.get(Integer.valueOf(mobeffect.getEffectId())));
            } else {
                this.effects.put(Integer.valueOf(mobeffect.getEffectId()), mobeffect);
                this.b(mobeffect);
            }
        }
    }

    public boolean a(MobEffect mobeffect) {
        if (this.getMonsterType() == MonsterType.UNDEAD) {
            int i = mobeffect.getEffectId();

            if (i == MobEffectList.REGENERATION.id || i == MobEffectList.POISON.id) {
                return false;
            }
        }

        return true;
    }

    public boolean aE() {
        return this.getMonsterType() == MonsterType.UNDEAD;
    }

    protected void b(MobEffect mobeffect) {
        this.b = true;
    }

    protected void c(MobEffect mobeffect) {
        this.b = true;
    }

    protected void d(MobEffect mobeffect) {
        this.b = true;
    }

    protected float G() {
        float f = 1.0F;

        if (this.hasEffect(MobEffectList.FASTER_MOVEMENT)) {
            f *= 1.0F + 0.2F * (float) (this.getEffect(MobEffectList.FASTER_MOVEMENT).getAmplifier() + 1);
        }

        if (this.hasEffect(MobEffectList.SLOWER_MOVEMENT)) {
            f *= 1.0F - 0.15F * (float) (this.getEffect(MobEffectList.SLOWER_MOVEMENT).getAmplifier() + 1);
        }

        return f;
    }

    public void enderTeleportTo(double d0, double d1, double d2) {
        this.setPositionRotation(d0, d1, d2, this.yaw, this.pitch);
    }

    public boolean isBaby() {
        return false;
    }

    public MonsterType getMonsterType() {
        return MonsterType.UNDEFINED;
    }

    public void c(ItemStack itemstack) {
        this.world.makeSound(this, "random.break", 0.8F, 0.8F + this.world.random.nextFloat() * 0.4F);

        for (int i = 0; i < 5; ++i) {
            Vec3D vec3d = Vec3D.create(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);

            vec3d.a(-this.pitch * 3.1415927F / 180.0F);
            vec3d.b(-this.yaw * 3.1415927F / 180.0F);
            Vec3D vec3d1 = Vec3D.create(((double) this.random.nextFloat() - 0.5D) * 0.3D, (double) (-this.random.nextFloat()) * 0.6D - 0.3D, 0.6D);

            vec3d1.a(-this.pitch * 3.1415927F / 180.0F);
            vec3d1.b(-this.yaw * 3.1415927F / 180.0F);
            vec3d1 = vec3d1.add(this.locX, this.locY + (double) this.y(), this.locZ);
            this.world.a("iconcrack_" + itemstack.getItem().id, vec3d1.a, vec3d1.b, vec3d1.c, vec3d.a, vec3d.b + 0.05D, vec3d.c);
        }
    }
}
