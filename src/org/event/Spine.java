// @wolfram77
package org.event;

// required modules
import org.data.*;
import java.util.*;
import java.util.concurrent.*;



/**
 * <b>Represents stimuli with associated reflexes</b>
 * <div>When a stimulus occurs, appropriate reflexes are triggered</div>
 * @author wolfram77
 */
public class Spine extends ConcurrentHashMap<String, Set<Reflexive>> {
    
    // static data
    static Reflexive fallback = new Reflexive() {
        @Override
        public void on(String stimulus, Map args) {
            System.out.println("["+stimulus+"] : "+args);
            if(args.containsKey("err")) throw new RuntimeException((Throwable)args.get("err"));
        }
    };
    
    
    /**
     * <b>Initialize reflex set of stimulus</b>
     * @param stimulus name of stimulus
     */
    void _initStimulus(String stimulus) {
        if(get(stimulus) != null) return;
        put(stimulus, Collections.newSetFromMap(new ConcurrentHashMap<Reflexive, Boolean>()));
    }
    
    
    /**
     * <b>Set fallback reaction, for stimulus with no reaction</b>
     * @param fallback fallback reaction
     */
    public static void fallback(Reflexive fallback) {
        Spine.fallback = fallback;
    }
    
    
    /**
     * <b>Get fallback reaction, for stimulus with no reaction</b>
     * @return fallback reaction
     */
    public static Reflexive fallback() {
        return fallback;
    }
    
    
    /**
     * <b>Get fallback reaction, for stimulus with no reaction</b>
     * @param reaction reaction that is slow (must be executed asynchronously)
     * @return fallback reaction
     */
    public static Reflexive slow(Reflexive reaction) {
        return (stimulus, args) -> threads.submit(() -> reaction.on(stimulus, args));
    }
    
    
    /**
     * <b>Create an Spine</b>
     * <div>Associate reactions to stimuli with {@code on(...)}</div>
     */
    public Spine() {
    }
    
    
    /**
     * <b>Indicate a stimulus, causing reactions to trigger</b>
     * @param stimulus name of stimulus
     * @param args additional arguments
     * @return spine for chaining
     */
    public Spine is(String stimulus, Map args) {
        Set<Reflexive> s = get(stimulus);
        if(s == null || s.isEmpty()) fallback.on(stimulus, args);
        else s.stream().forEach((r) -> {
            r.on(stimulus, args);
        });
        return this;
    }
    
    
    /**
     * <b>Indicate a stimulus, causing reactions to trigger</b>
     * @param stimulus name of stimulus
     * @param args additional arguments
     * @return spine for chaining
     */
    public Spine is(String stimulus, Object... args) {
        return is(stimulus, Coll.map(args));
    }
    
    
    /**
     * <b>Set a reaction to trigger on a stimulus</b>
     * @param stimulus name of stimulus
     * @param reaction reaction to trigger
     * @return spine for chaining
     */
    public Spine on(String stimulus, Reflexive reaction) {
        _initStimulus(stimulus);
        get(stimulus).add(reaction);
        return this;
    }
    
    
    /**
     * <b>Set reactions to trigger on a stimulus</b>
     * @param stimulus name of stimulus
     * @param reactions reactions to trigger
     * @return spine for chaining
     */
    public Spine on(String stimulus, Collection<Reflexive> reactions) {
        _initStimulus(stimulus);
        get(stimulus).addAll(reactions);
        return this;
    }
    
    
    /**
     * <b>Set a reaction to trigger on stimuli</b>
     * @param stimuli names of stimuli
     * @param reaction reaction to trigger
     * @return spine for chaining
     */
    public Spine on(Collection<String> stimuli, Reflexive reaction) {
        stimuli.stream().forEach((stim) -> {
            on(stim, reaction);
        });
        return this;
    }
    
    
    /**
     * <b>Set associated reactions to trigger on stimuli</b>
     * @param assoc stimuli with associated reactions to trigger
     * @return spine for chaining
     */
    public Spine on(Map<String, ? extends Collection<Reflexive>> assoc) {
        assoc.forEach((stimulus, reaction) -> on(stimulus, reaction));
        return this;
    }
    
    
    /**
     * <b>Turn off a reaction for a stimulus</b>
     * @param stimulus name of stimulus
     * @param reaction reaction to turn off
     * @return spine for chaining
     */
    public Spine off(String stimulus, Reflexive reaction) {
        Set<Reflexive> s = get(stimulus);
        if(s != null) s.remove(reaction);
        return this;
    }
    
    
    /**
     * <b>Turn off reactions for a stimulus</b>
     * @param stimulus name of stimulus
     * @param reactions reactions to turn off
     * @return spine for chaining
     */
    public Spine off(String stimulus, Collection<Reflexive> reactions) {
        Set<Reflexive> s = get(stimulus);
        if(s != null) s.removeAll(reactions);
        return this;
    }
    
    
    /**
     * <b>Turn off a reaction for stimuli</b>
     * @param stimuli names of stimuli
     * @param reaction reaction to turn off
     * @return spine for chaining
     */
    public Spine off(Collection<String> stimuli, Reflexive reaction) {
        stimuli.stream().forEach((stim) -> {
            off(stim, reaction);
        });
        return this;
    }
    
    
    /**
     * <b>Turn off associated reactions for stimuli</b>
     * @param assoc stimuli with associated reactions to turn off
     * @return spine for chaining
     */
    public Spine off(Map<String, ? extends Collection<Reflexive>> assoc) {
        assoc.forEach((stimulus, reaction) -> off(stimulus, reaction));
        return this;
    }
    
    
    /**
     * <b>Turn off all reactions for a stimulus</b>
     * @param stimulus name of stimulus
     * @return spine for chaining
     */
    public Spine off(String stimulus) {
        remove(stimulus);
        return this;
    }
    
    
    /**
     * <b>Turn off all reactions for all stimuli</b>
     * @return spine for chaining
     */
    public Spine off() {
        clear();
        return this;
    }
}
